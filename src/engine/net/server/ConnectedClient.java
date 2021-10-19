package engine.net.server;

import engine.consts.NetworkConstants;
import engine.input.Input;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedClient extends Thread implements NetworkConstants {

    private Server server;
    private String hostAdress;

    // Network
    private Socket tcpSocket;
    private int clientId;
    private BufferedReader in;
    private BufferedWriter out;
    private DatagramSocket udpSocket;
    private DatagramPacket sendPacket;
    private InetAddress address;
    private byte buf[];
    private boolean verified; // Used to differenciate real connection to fake / lan ping ones

    private Input input;

    /**
     * Initialize the attributs
     */
    public ConnectedClient(String ip, Socket tcpSocket, Server server, int clientId) {
        super(ip);
        try {
            input = new Input();
            this.tcpSocket = tcpSocket;
            this.server = server;
            this.clientId = clientId;
            hostAdress = tcpSocket.getInetAddress().getHostAddress();
            udpSocket = new DatagramSocket();
            address = InetAddress.getByName(ip);
            buf = new byte[65535];
            verified = false;
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Wait until it receives a TCP packet and send it to be processed In case
     * of "null" packet, then connection is lost so disconnect
     */
    @Override
    public void run() {
        String stringInput;
        boolean clientConnected = true;

        try {
            in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream()));
            while (clientConnected) {
                stringInput = in.readLine();
                if (stringInput != null) {
                    server.addPacketToQueue(stringInput, clientId);
                } else {
                    clientConnected = false;
                }
            }
            Server.waitTime(100);
            clientConnected = false;
            if (verified) {
                server.writeConsole("#o" + hostAdress + " (" + clientId + ") Has Left");
            }
            tcpSocket.close();
            server.removeClient(clientId);

        } catch (IOException ex) {
            Server.waitTime(100);
            clientConnected = false;
            if (verified) {
                server.writeConsole("#o" + hostAdress + " (" + clientId + ") Has Left");
            }
            server.removeClient(clientId);
        } catch (Exception e) {
        }
    }

    /**
     * Sends a packet to the client via TCP
     *
     * @param txt The text to send
     * @param cibled True if the packet is only for this client (Not broadcast)
     */
    public void sendTCPPacket(String txt, boolean cibled) {
        try {
            if (tcpSocket != null) {
                if (!tcpSocket.isClosed()) {
                    txt = server.encryptData(txt);
                    out.write(txt + "\n");
                    out.flush();
                }
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Sends a packet to the client via UDP
     *
     * @param txt The text to send
     */
    public void sendUDPPacket(String txt) {
        try {
            txt = server.encryptData(txt);
            buf = txt.getBytes();
            sendPacket = new DatagramPacket(buf, buf.length, address, UDP_CLT_PORT);
            udpSocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reset all it's networking attributs
     */
    public void discard() {
        try {
            if (verified) {
                if (server.getEventController() != null) {
                    server.getEventController().onUserDisconnect(this);
                }
            }
            tcpSocket.close();
            tcpSocket = null;
            in = null;
            out = null;
        } catch (IOException ex) {
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHostAdress() {
        return hostAdress;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Input getInput() {
        return input;
    }

}
