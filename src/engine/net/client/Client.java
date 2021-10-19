package engine.net.client;

import engine.GameContainer;
import engine.consts.NetworkConstants;
import engine.ihm.net.client.ClientConsole;
import engine.net.client.events.ClientEventController;
import engine.net.packets.Packet;
import static engine.net.packets.Packet.PacketTypes.CLIENTID;
import static engine.net.packets.Packet.PacketTypes.CLOSING_SERVER;
import static engine.net.packets.Packet.PacketTypes.INVALID;
import static engine.net.packets.Packet.PacketTypes.MESSAGE;
import engine.net.packets.gameObject.PacketAddGameObject;
import engine.net.packets.gameObject.PacketRemoveGameObject;
import engine.net.packets.gameObject.PacketUpdateGameObject;
import engine.net.packets.inputs.PacketInput;
import engine.net.packets.net.PacketClientId;
import engine.net.packets.net.PacketCommand;
import engine.net.packets.net.PacketConnect;
import engine.net.packets.net.PacketMessage;
import engine.net.packets.net.PacketPingMs;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements NetworkConstants {

    // Utils
    private final GameContainer gc;
    private final ClientConsole console;
    private final TCPClient tcpClient;
    private final UDPClient udpClient;
    private PacketInput inputs;
    private volatile boolean connected;
    private int clientId;
    private long currentPingMs;
    private ClientEventController eventController;

    /**
     * Initialize the connected, tcp&udp Client attributs
     *
     * @param gc
     */
    public Client(GameContainer gc) {
        this.gc = gc;
        eventController = null;
        console = new ClientConsole(gc);
        tcpClient = new TCPClient(this);
        udpClient = new UDPClient(this);
        connected = false;
        console.writeConsole("#c- #wClient LAN #c- ");
        console.writeConsole(" - Du simple texte est envoyé comme message au server");
        console.writeConsole(" - Du texte commençant par #g\"/\" #west envoyé comme commande au server");
        console.writeConsole(" - Les commandes sont constituées de 2 parties (i.e => #oset name #bexemple)");
    }

    /**
     * Tries to connect a server using an ip and a port using TCP Socket, sets
     * connected as true in case of success and sets the ip address of the
     * server into the udp server
     *
     * @param ip The ip of the server
     * @return True if there is an error
     */
    public boolean connect(String ip) {
        if (ip == null || ip.length() == 0) {
            return true;
        }
        boolean error = tcpClient.connect(ip);
        udpClient.setUDPSocketIP(tcpClient.getServerIP());
        tcpClient.sendTCPPacket(new PacketConnect(gc.getSettings().getGameVersion()).getData());
        if (!error) {
            connected = true;
            inputs = new PacketInput();
            switchCoDiscoButton();
        }
        return error;
    }

    /**
     * Tries to disconnect the TCP Socket and sets connected as false in case of
     * success
     *
     * @return True if there is an error
     */
    public boolean disconnect() {
        boolean error = tcpClient.disconnect();
        if (!error) {
            connected = false;
            switchCoDiscoButton();
            if (eventController != null) {
                eventController.onDisconnect();
            }
        }
        return error;
    }

    /**
     * Appends text to the console
     *
     * @param txt The text to write
     */
    public void writeConsole(String txt) {
        console.writeConsole(txt);
    }

    /**
     * Wait an amount a miliseconds
     *
     * @param ms The amount of time to wait
     */
    public static void waitTime(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendInput(int VK, boolean pressed) {
        inputs.update(VK, pressed);
        sendTCPPacket(inputs.getData());
    }

    /**
     * Sends a TCP packet to the server
     *
     * @param txt The text to send
     */
    public void sendTCPPacket(String txt) {
        tcpClient.sendTCPPacket(txt);
    }

    /**
     * Sends a UDP packet to the server
     *
     * @param txt The text to send
     */
    public void sendUDPPacket(String txt) {
        udpClient.sendUDPPacket(txt);
    }

    /**
     * Sets a new Port for the UDP Socket
     *
     * @param ip the Ip of the server
     */
    public void setUDPSocketIP(String ip) {
        udpClient.setUDPSocketIP(ip);
    }

    /**
     * Switches the button & title
     */
    public void switchCoDiscoButton() {
        console.switchCoDiscoButton();
    }

    /**
     * Sends a simple message packet
     *
     * @param txt The message
     */
    public void sendMessage(String txt) {
        sendTCPPacket(new PacketMessage(txt).getData());
    }

    /**
     * Sends a command packet
     *
     * @param txt The command
     */
    public void sendCommand(String txt) {
        if (txt.startsWith("/connect")) {
            connect(txt.replace("/connect ", ""));
        } else {
            sendTCPPacket(new PacketCommand(txt, clientId).getData());
        }
    }

    /**
     * There is an error in the server, write it and disconnect from it
     */
    public void serverError() {
        writeConsole("#rServer Error - Disconnecting");
        disconnect();
    }

    /**
     * Find the corresponding packet to an abv
     *
     * @param abv The abv to processed
     * @return The PacketType
     */
    public static Packet.PacketTypes findPacketType(String abv) {
        for (Packet.PacketTypes pt : Packet.PacketTypes.values()) {
            if (pt.getAbv().equals(abv)) {
                return pt;
            }
        }
        return null;
    }

    /**
     * Processes a Packet by finding it's corresponding type
     *
     * @param input The full packet containing it's abv
     */
    protected void processPacket(String input) {
        input = decryptData(input);
        if (input == null) {
            // null if there was an error decrypting the packet
            return;
        }
        String abv = input.substring(0, 3);
        input = input.substring(3);

        if (findPacketType(abv) == null) {
            if (eventController != null) {
                eventController.onCustomPacket(abv, input);
            }
            return;
        }
        switch (findPacketType(abv)) {
            case INVALID:
                break;
            case CLIENTID:
                new PacketClientId().processData(input, this);
                if (eventController != null) {
                    eventController.onConnect();
                }
                break;
            case CLOSING_SERVER:
                serverError();
                break;
            case MESSAGE:
                writeConsole(input);
                break;
            case PING_MS:
                new PacketPingMs(gc).processData(input);
                break;
            case ADD_GAME_OBJECT:
                new PacketAddGameObject(gc).processData(input);
                break;
            case UPDATE_GAME_OBJECT:
                new PacketUpdateGameObject(gc).processData(input);
                break;
            case REMOVE_GAME_OBJECT:
                new PacketRemoveGameObject(gc).processData(input);
                break;
            case DISCONNECTING:
                if (!disconnect()) {
                    writeConsole("#oDisconnected from the server");
                }
                break;
            default:
                if (eventController != null) {
                    eventController.onCustomPacket(abv, input);
                }
                break;
        }
    }

    public String encryptData(String txt) {
        if (txt != null && !txt.isEmpty()) {
            if (gc.getNetworkSecurity() != null) {
                return gc.getNetworkSecurity().encryptData(gc, txt);
            }
        }
        return txt;
    }

    public String decryptData(String txt) {
        if (txt != null && !txt.isEmpty()) {
            if (gc.getNetworkSecurity() != null) {
                txt = gc.getNetworkSecurity().decryptData(gc, txt);
                return txt;
            }
        }
        return txt;
    }

    public String decryptData(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            if (gc.getNetworkSecurity() != null) {
                String s = gc.getNetworkSecurity().decryptData(gc, bytes);
                return s;
            }
        }
        return null;
    }

    public void execute(String text) {
        if ((text != null) && (!text.isEmpty())) {
            if (text.startsWith("/")) {
                sendCommand(text);
            } else {
                sendMessage(text);
            }
        }
    }

    public void addEventListener(ClientEventController e) {
        if (e == null) {
            return;
        }
        eventController = e;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean switchConnectionState(String ip) {
        return isConnected() ? disconnect() : connect(ip);

    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }

    public GameContainer getGc() {
        return gc;
    }

    public ClientEventController getEventController() {
        return eventController;
    }

    public ClientConsole getConsole() {
        return console;
    }

    public long getCurrentPingMs() {
        return currentPingMs;
    }

    public void setCurrentPingMs(long currentPingMs) {
        this.currentPingMs = currentPingMs;
    }

}
