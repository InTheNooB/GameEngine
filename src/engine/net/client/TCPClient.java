package engine.net.client;

import engine.consts.NetworkConstants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class TCPClient extends Thread implements NetworkConstants {

    private volatile Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private final Client client;
    private String serverIP;

    /**
     * Initialize mainClient attribut
     *
     * @param client
     */
    public TCPClient(Client client) {
        super("TCP Thread");
        this.client = client;
        this.start();
    }

    /**
     * Tries to connect the a server using an IP and a Port
     *
     * @param ip The ip of the server
     * @return True if there is an error
     */
    public boolean connect(String ip) {
        boolean error = false;
        try {
            SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(ip), TCP_PORT);
            serverIP = ip;
            socket = new Socket();
            socket.connect(sockaddr, 1000);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            client.writeConsole("#gConnection Established");
        } catch (SocketTimeoutException ex) {
            // Rien car timeout
            client.writeConsole("#oUnknown Server");
            error = true;
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        }
        return error;
    }

    /**
     * Waits until it receives a TCP packet from the server and processes it
     */
    @Override
    public void run() {
        String input;
        while (true) {
            while (isConnected()) {
                try {
                    if ((socket != null) && (in != null)) {
                        if ((socket.isConnected()) && !((socket.isClosed()))) {
                            input = in.readLine();
                            if (input != null) {
                                client.processPacket(input);
                            } else {
                                client.serverError();
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
            Client.waitTime(500);
        }
    }

    /**
     * Tries to disconnect from the server
     *
     * @return True if there is an error
     */
    public boolean disconnect() {
        boolean error = false;
        if (socket != null) {
            try {
                socket.close();
                socket = null;
                in = null;
                out = null;
                client.writeConsole("#gSuccesfully Disconnected");
            } catch (IOException ex) {
                client.writeConsole("#rError While Disconnecting");
                error = true;
            }
        }
        return error;
    }

    /**
     * Sends a text to the server via TCP
     *
     * @param txt The text to send
     */
    public void sendTCPPacket(String txt) {
        try {
            if ((out != null) && (!socket.isClosed())) {
                txt = client.encryptData(txt);
                out.write(txt + "\n");
                out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the IP Address of the server
     *
     * @return The IP address
     */
    public String getServerIP() {
        return serverIP;
    }

    private boolean isConnected() {
        return client.isConnected();
    }

}
