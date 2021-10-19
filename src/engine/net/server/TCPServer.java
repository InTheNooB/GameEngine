package engine.net.server;

import engine.consts.NetworkConstants;
import engine.net.packets.Packet.PacketTypes;
import engine.net.packets.net.PacketClosingServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer extends Thread implements NetworkConstants {

    private volatile ServerSocket tcpSocket;
    private volatile ConcurrentHashMap<Integer, ConnectedClient> clients;
    private int clientCounter;
    private final Server server;

    /**
     * Initialize the clients, clientCounter and the server
     *
     * @param server
     */
    public TCPServer(Server server) {
        super("TCP Thread");
        clients = new ConcurrentHashMap();
        clientCounter = 1;
        this.server = server;
    }

    /**
     * Starts the server by creating the ServerSocket using the port constant
     * attribut
     *
     * @return True if there is an error
     */
    public boolean startServer() {
        boolean error = false;
        try {
            tcpSocket = new ServerSocket(TCP_PORT);
            tcpSocket.setSoTimeout(1000);
        } catch (IOException exc) {
            error = true;
        }
        return error;
    }

    /**
     * Stops the server by closing the ServerSocket and removing each remaining
     * clients
     *
     * @return True if there is an error
     */
    public boolean stopServer() {
        boolean error = false;
        if (tcpSocket != null) {
            try {
                sendTCPPacket(PacketTypes.CLOSING_SERVER);
                tcpSocket.close();
                for (ConnectedClient c : clients.values()) {
                    c.discard();
                }

                clients.clear();
            } catch (IOException ex) {
                ex.printStackTrace();
                error = true;
                server.writeConsole("#rError Stopping The Server");
            }
        }
        return error;
    }

    /**
     * The TCPServer Server is meant to acknowledge a connection/disconnection
     * This method waits until someone tries to connect, and then add him as a
     * client
     *
     */
    @Override
    public void run() {
        ConnectedClient client;
        Socket socketClient;
        while (true) {
            while (server.isRunning()) {
                try {
                    socketClient = getServerSocket().accept();
                    Server.waitTime(100);
                    client = new ConnectedClient(socketClient.getInetAddress().getHostAddress(), socketClient, server, clientCounter);
                    clients.put(clientCounter, client);
                    client.start();
                    clientCounter++;
                } catch (SocketTimeoutException ex) {
                    // rien car timeout
                } catch (IOException exc) {
                }
                if (!server.isRunning()) {
                    server.writeConsole("#rServer Closed");
                }
            }
            Server.waitTime(500);
        }
    }

    public void initNewClient(int clientId) {
        ConnectedClient c = clients.get((Integer) clientId);
        server.writeConsole("New Client < #b" + c.getHostAdress() + " #-> - ID : #b" + clientId + " #- - Total : " + clients.size());
        Server.waitTime(10);
        server.addClient(c, clientId);
        c.setVerified(true);
    }

    /**
     * Sends a TCPServer Packet to every client
     *
     * @param txt The text to send
     */
    public void sendTCPPacket(String txt) {
        for (ConnectedClient c : clients.values()) {
            c.sendTCPPacket(txt, false);
        }
    }

    /**
     * Sends a TCPServer Packet to the specified client
     *
     * @param c The client
     * @param txt The text to send
     */
    public void sendTCPPacket(ConnectedClient c, String txt) {
        c.sendTCPPacket(txt, true);
    }

    /**
     * Sends a TCPServer Packet to the specified client
     *
     * @param clientId The client
     * @param txt The text to send
     */
    public void sendTCPPacket(int clientId, String txt) {
        ConnectedClient c = clients.get((Integer) clientId);
        if (c != null) {
            c.sendTCPPacket(txt, true);
        } else {
            server.getGc().getEventHistory().addEvent("Client Not Found : TCPServer.sendTCPPacket(clientId, txt) => \n"
                    + "txt : " + txt + "\n"
                    + "clientId : " + clientId);
        }
    }

    /**
     * Sends a packet to every clients
     *
     * @param p The type of the packet
     */
    private void sendTCPPacket(PacketTypes p) {
        switch (p) {
            case CLOSING_SERVER:
                sendTCPPacket(new PacketClosingServer().getData());
                break;
        }
    }

    /**
     * @return The IP address of the server
     */
    public String getHostIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "0.0.0.0";
    }

    private synchronized ServerSocket getServerSocket() {
        return tcpSocket;
    }

    public ConcurrentHashMap<Integer, ConnectedClient> getClients() {
        return clients;
    }

}
