package engine.net.client;

import engine.GameContainer;
import engine.consts.NetworkConstants;
import static engine.consts.NetworkConstants.UDP_CLT_BROADCAST_LOOKUP_PORT;
import static engine.consts.NetworkConstants.UDP_SRV_PORT;
import engine.net.packets.net.PacketServerName;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkScanner implements NetworkConstants {

    // timeout to add after each found server
    private static final long TIMEOUT = 500;

    public NetworkScanner() {
    }

    /*
    public static ArrayList<String> scan(GameContainer gc) throws IOException, InterruptedException, ExecutionException {
        ArrayList<String> ips = new ArrayList();
        ips.add("No Server Found");
        final ExecutorService es = Executors.newFixedThreadPool(20);
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String name;
        final int timeout = 20;
        byte[] ip = localhost.getAddress();
        for (int i = 160; i < 175; i++) {
            ip[3] = (byte) i;
            InetAddress address = InetAddress.getByAddress(ip);
            name = portIsOpen(gc, es, address.getHostAddress(), TCP_PORT, timeout);
            if (name != null) {
                ips.add(name + "  >  " + address.getHostAddress());
            }
        }
        es.shutdown();
        if (ips.size() != 1) {
            ips.remove(0);
        }
        return ips;
    }

    public static String portIsOpen(GameContainer gc, final ExecutorService es, final String ip, final int port, final int timeout) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            out.println(new PacketServerName(null).getData());
            out.flush();
            String pck = in.readLine().substring(3);
            String name = pck.split(";")[0];
            String gameVersion = pck.split(";")[1];
            socket.close();
            if (!gameVersion.equals(gc.getSettings().getGameVersion())) {
                return null;
            } else {
                return name;
            }
        } catch (Exception ex) {
            return null;
        }
    }
     */
    public static ArrayList<String> broadcastUDPLookupServer(GameContainer gc) {

        ArrayList<String> servers = new ArrayList();

        // Packet for receiving response from server
        byte[] receiveBuffer = new byte[65535];
        DatagramPacket receiveBroadcastPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        // Create a Datagram (UDP) socket on any available port
        DatagramSocket broadcastSocket = createSocket();

        // send a known request string (server checks this)
        byte[] packetData = gc.getClient().encryptData(new PacketServerName(null).getData()).getBytes();

        // try the widest broadcast address first
        InetAddress broadcastAddress = null;
        try {
            broadcastAddress = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            /* This should never happen! */
        }
        DatagramPacket packet = new DatagramPacket(packetData,
                packetData.length, broadcastAddress, UDP_SRV_PORT);

        long time = System.currentTimeMillis();
//        ArrayList<InetAddress> addresses = new ArrayList();

        try {
            broadcastSocket.send(packet);
            while (true) {
                broadcastSocket.receive(receiveBroadcastPacket);

//                if (addresses.contains(receiveBroadcastPacket.getAddress())) {
//                if (System.currentTimeMillis() - time > TIMEOUT) {
//                    broadcastSocket.close();
//                    break;
//                } else {
//                    continue;
//                }
//                } else {
//                    addresses.add(receiveBroadcastPacket.getAddress());
//                }
//                System.out.println("RECEIVED : " + Arrays.toString(receiveBroadcastPacket.getData()));
//                System.out.println(Arrays.toString(new String(receiveBroadcastPacket.getData(), 0, receiveBroadcastPacket.getLength()).getBytes()));
//                String reply = gc.getClient().decryptData(receiveBroadcastPacket.getData());
                String reply = new String(receiveBroadcastPacket.getData(), 0, receiveBroadcastPacket.getLength());
                if (reply.startsWith("SVN")) {
                    reply = reply.substring(3);
                    String name = reply.split(";")[0];
                    String gameVersion = reply.split(";")[1];
                    InetAddress address = receiveBroadcastPacket.getAddress();
                    if (gameVersion.equals(gc.getSettings().getGameVersion())) {
                        servers.add(name + "  >  " + address.getHostAddress());
                        time = System.currentTimeMillis();
                    }
                }
                if (System.currentTimeMillis() - time > TIMEOUT) {
                    broadcastSocket.close();
                    break;
                }
            }
        } catch (IOException ex) {
            broadcastSocket.close();
        }
        if (servers.isEmpty()) {
            servers.add("No Server Found");
        }
        return servers;
    }

    public static DatagramSocket createSocket() {
        // Create a Datagram (UDP) socket on any available port
        DatagramSocket socket = null;
        // Create a socket for sending UDP broadcast packets
        try {
            socket = new DatagramSocket(UDP_CLT_BROADCAST_LOOKUP_PORT);
            socket.setBroadcast(true);
            // use a timeout and resend broadcasts instead of waiting forever
            socket.setSoTimeout((int) TIMEOUT);
        } catch (SocketException sex) {
            sex.printStackTrace();
        }
        return socket;
    }

}
