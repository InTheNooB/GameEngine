/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import engine.consts.NetworkConstants;
import engine.net.packets.net.PacketServerName;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author lione
 */
public class UDPServer extends Thread implements NetworkConstants {

    private DatagramSocket udpSocket;
    private DatagramPacket receivePacket;
    private byte[] buf;
    private final Server server;

    /**
     * Starts the socket by creating a new DatagramSocket using the
     * UDP_SOCKET_PORT as port
     *
     * Also sets the mainServer
     *
     * @param server
     */
    public UDPServer(Server server) {
        super("UDP Thread");
        try {
            udpSocket = new DatagramSocket(UDP_SRV_PORT, InetAddress.getByName("0.0.0.0"));
            udpSocket.setBroadcast(true);
        } catch (SocketException ex) {
            server.writeConsole("A Server Is Already Running On This Computer");
            System.exit(0);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.server = server;
        buf = new byte[65535];
    }

    @Override
    public void run() {
        InetAddress ip;
        String input, decryptedInput;
        int port;
        while (true) {
            while (isRunning()) {
                try {
                    receivePacket = new DatagramPacket(buf, buf.length);
                    udpSocket.receive(receivePacket);
                    ip = receivePacket.getAddress();
                    port = receivePacket.getPort();
                    input = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    decryptedInput = server.decryptData(input);
                    if (decryptedInput == null) {
                        continue;
                    }
                    // Broadcast packet to find the server
                    if (decryptedInput.startsWith("SVN")) {
                        byte[] sendData = new PacketServerName(server).getData().getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
                        udpSocket.send(sendPacket);
                    } else {
                        server.processPacket(decryptedInput, ip.getHostAddress());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!isRunning()) {
                    udpSocket.close();
                }
            }
            Server.waitTime(500);
        }
    }

    /**
     * Sends a UDPServer Packet to every client
     *
     * @param txt The text to send
     */
    public void sendUDPPacket(String txt) {
        for (ConnectedClient c : server.getClients().values()) {
            c.sendUDPPacket(txt);
        }
    }

    /**
     * Sends a UDPServer Packet to a specific client
     *
     * @param cc The client
     * @param txt The Packet
     */
    public void sendUDPPacket(ConnectedClient cc, String txt) {
        cc.sendUDPPacket(txt);
    }

    private boolean isRunning() {
        return server.isRunning();
    }

}
