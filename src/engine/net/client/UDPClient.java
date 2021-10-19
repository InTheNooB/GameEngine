package engine.net.client;

import engine.consts.NetworkConstants;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPClient extends Thread implements NetworkConstants {

    private DatagramSocket socket;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private InetAddress address;
    private Client client;

    private byte[] bufSend;
    private byte[] bufReceive;

    public UDPClient(Client client) {
        super("UDP Thread");
        this.client = client;
        bufSend = new byte[65535];
        bufReceive = new byte[65535];
        try {
            socket = new DatagramSocket(UDP_CLT_PORT);
        } catch (SocketException ex) {
            client.writeConsole("A Client Is Already Running On This Computer");
            System.exit(0);
        }
        this.start();
    }

    /**
     * Wait until it receives a UDP Packet from the server
     */
    @Override
    public void run() {
        String received;
        while (true) {
            if (socket != null && !socket.isClosed()) {
                try {
                    receivePacket = new DatagramPacket(bufReceive, bufReceive.length);
                    socket.receive(receivePacket);
                    received = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    client.processPacket(received);
                } catch (IOException ex) {
                    Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Client.waitTime(500);
            }
        }
    }

    /**
     * Sends a UDP Packet to the server
     *
     * @param txt The txt to send
     */
    public void sendUDPPacket(String txt) {
        if (socket != null) {
            try {
                System.out.println("Sent pre : " + txt);
                txt = client.encryptData(txt);
                System.out.println("Sent post : " + txt);
                bufSend = txt.getBytes();
                sendPacket = new DatagramPacket(bufSend, bufSend.length, address, UDP_SRV_PORT);
                socket.send(sendPacket);
            } catch (IOException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Closes the UDP Socket
     */
    public void close() {
        socket.close();
    }

    /**
     * Sets a socket Port to the DatagramSocket
     *
     * @param ip The Ip address of the server
     */
    public void setUDPSocketIP(String ip) {
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
