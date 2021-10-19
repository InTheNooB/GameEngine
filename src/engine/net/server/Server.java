package engine.net.server;

import engine.GameContainer;
import engine.consts.NetworkConstants;
import engine.ihm.net.server.ServerConsole;
import engine.net.packets.Packet;
import engine.net.packets.Packet.PacketTypes;
import static engine.net.packets.Packet.PacketTypes.INVALID;
import engine.net.packets.inputs.PacketInput;
import engine.net.packets.net.PacketClientId;
import engine.net.packets.net.PacketCommand;
import engine.net.packets.net.PacketConnect;
import engine.net.packets.net.PacketDisconnectedClient;
import engine.net.packets.net.PacketPingMs;
import engine.net.packets.net.PacketServerName;
import engine.net.server.commands.Command;
import engine.net.server.commands.Command.CommandType;
import engine.net.server.commands.CommandHelp;
import engine.net.server.commands.CommandMessagePlayer;
import engine.net.server.commands.CommandServerAnnounce;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import engine.net.server.events.ServerEventController;
import engine.net.server.inputProcessing.InputProcessingThread;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server implements NetworkConstants {

    // Network
    private volatile TCPServer tcp;
    private volatile UDPServer udp;

    // Utils
    private volatile boolean running;
    private final GameContainer gc;
    private long currentPingMs;

    // Input Packets Processing
    private BlockingQueue<Runnable> blockingQueue;
    private ThreadPoolExecutor executor;

    // Console
    private final ServerConsole console;

    // Event
    private ServerEventController eventController;

    /**
     * Initialize both servers (UDPServer & TCPServer)
     *
     * @param gc
     * @throws SocketException
     */
    public Server(GameContainer gc) throws SocketException {
        this.gc = gc;

        // Servers
        tcp = new TCPServer(this);
        udp = new UDPServer(this);

        // Process Inbound packets
        setupThreadPool();

        console = new ServerConsole(gc);
        eventController = null;
    }

    private void setupThreadPool() {
        blockingQueue = new ArrayBlockingQueue(50);
        executor = new ThreadPoolExecutor(10, 20, 5000, TimeUnit.MILLISECONDS, blockingQueue);

        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executor.execute(r);
            }
        });

        executor.prestartAllCoreThreads();
    }

    /**
     * Starts the TCPServer Server and set running as true
     *
     * @return True if there is an error
     */
    public boolean startServer() {
        boolean error = tcp.startServer();
        if (!error) {
            if (!tcp.isAlive()) {
                tcp.start();
            }
            if (!udp.isAlive()) {
                udp.start();
            }
            // The server started correctly
            running = true;
            console.setVisible(true);
            writeConsole("-----------------");
            writeConsole("#gServer Started");
            writeConsole("-----------------");
            writeConsole("Name : #g" + gc.getSettings().getServerName());
            writeConsole("IP : #g" + tcp.getHostIp());
            writeConsole("Server TCP Listening Port : #g" + TCP_PORT);
            writeConsole("Server UDP Listening Port : #g" + UDP_SRV_PORT);
            writeConsole("Client UDP Listening Port : #g" + UDP_CLT_PORT);
            writeConsole("Game Version : \' #g" + gc.getSettings().getGameVersion() + " #w\'");
            writeConsole("-----------------");

        }
        return error;
    }

    /**
     * Stops the TCPServer Server and set running as false
     *
     * @return True if there is an error
     */
    public boolean stopServer() {
        running = false;
        executor.shutdownNow();
        return tcp.stopServer();
    }

    public void writeConsole(String txt) {
        if (console != null) {
            console.writeConsole(txt);
        }
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
            Logger.getLogger(ConnectedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPacketToQueue(String input, int clientId) {
        executor.execute(new InputProcessingThread(input, clientId, gc));
    }

    /**
     * Processes a Packet by finding it's corresponding type
     *
     * @param input The full packet containing it's abv
     * @param clientId The client's id
     */
    public void processPacket(String input, int clientId) {
        input = decryptData(input);
        if (input == null) {
            // null if there was an error decrypting the packet
            return;
        }
        String abv = input.substring(0, 3);
        input = input.substring(3);
        if (findPacketType(abv) == null) {
            if (eventController != null) {
                eventController.onCustomPacket(tcp.getClients().get((Integer) clientId), abv, input);
            }
            return;
        }
        switch (findPacketType(abv)) {
            case MESSAGE:
                writeConsole("<" + clientId + "> " + input);
                break;
            case COMMAND:
                new PacketCommand(this).processData(input);
                break;
            case SERVER_NAME:
                sendTCPPacket(clientId, new PacketServerName(this).getData());
                break;
            case CONNECTING:
                new PacketConnect(this, clientId).processData(input);
                break;
            case PING_MS:
                sendTCPPacket(clientId, new PacketPingMs(input).getData());
                break;
            case INPUT:
                new PacketInput(this).processData(input, clientId);
                break;
            case INVALID:
                break;
            default:
                if (eventController != null) {
                    eventController.onCustomPacket(tcp.getClients().get((Integer) clientId), abv, input);
                }
                break;

        }
    }

    /**
     * Finds the corresponding clientId depending on the clientIP and then calls
     * the processPacket function
     *
     * @param input The full packet
     * @param clientIP The client's IP
     */
    public void processPacket(String input, String clientIP) {
        processPacket(input, lookupClient(clientIP));
    }

    /**
     * Find the corresponding packet to an abv
     *
     * @param abv The abv to processed
     * @return The PacketType
     */
    private PacketTypes findPacketType(String abv) {
        for (Packet.PacketTypes pt : Packet.PacketTypes.values()) {
            if (pt.getAbv().equals(abv)) {
                return pt;
            }
        }
        return null;
    }

    /**
     * Adds a client into the client list and sends him starting informations
     *
     * @param client The client
     * @param clientId It's id
     */
    public void addClient(ConnectedClient client, int clientId) {
        sendTCPPacket(client, new PacketClientId(clientId).getData());
        if (eventController != null) {
            eventController.onUserConnect(tcp.getClients().get((Integer) clientId));
        }
    }

    /**
     * Removes a client from the arraylist of client
     *
     * @param clientId
     */
    public void removeClient(int clientId) {
        ConnectedClient c = tcp.getClients().get((Integer) clientId);
        if (c != null) {
            boolean verified = c.isVerified();
            if (verified) {
                if (eventController != null) {
                    eventController.onUserDisconnect(tcp.getClients().get((Integer) clientId));
                }
            }
            tcp.sendTCPPacket(new PacketDisconnectedClient(c.getClientId()).getData());
            tcp.getClients().remove(clientId);
        } else {
            tcp.sendTCPPacket(new PacketDisconnectedClient(1000).getData());
        }
    }

    /**
     * Sends a TCPServer Packet to every client via the TCPServer
     *
     * @param txt The text to send
     */
    public void sendTCPPacket(String txt) {
        tcp.sendTCPPacket(txt);
    }

    /**
     * Sends a TCPServer Packet to the specified client
     *
     * @param c The client
     * @param txt The text to send
     */
    public void sendTCPPacket(ConnectedClient c, String txt) {
        tcp.sendTCPPacket(c, txt);
    }

    /**
     * Sends a TCPServer Packet to the specified client
     *
     * @param clientId
     * @param txt The text to send
     */
    public void sendTCPPacket(int clientId, String txt) {
        tcp.sendTCPPacket(clientId, txt);
    }

    /**
     * Sends a UDPServer Packet to every client via the UDPServer
     *
     * @param txt The text to send
     */
    public void sendUDPPacket(String txt) {
        udp.sendUDPPacket(txt);
    }

    /**
     * Sends a UDPServer Packet to a specific client via the UDPServer
     *
     * @param cc The client to send to
     * @param txt The text to send
     */
    public void sendUDPPacket(ConnectedClient cc, String txt) {
        udp.sendUDPPacket(cc, txt);
    }

    /**
     * Inits a new player and some attributs
     *
     * @param gameVersion
     * @param clientId
     */
    public void initNewClient(String gameVersion, int clientId) {
        if (gameVersion.equals(gc.getSettings().getGameVersion())) {
            tcp.initNewClient(clientId);
        } else {
            removeClient(clientId);
        }
    }

    /**
     * Finds the corresponding clientId from a clientIP
     *
     * @param clientIP The client IP
     * @return The client Id
     */
    private int lookupClient(String clientIP) {
        for (ConnectedClient c : tcp.getClients().values()) {
            if (c.getHostAdress().equals(clientIP)) {
                return c.getClientId();
            }
        }
        return -1;
    }

    public void processReceivedInput(int key, boolean pressed, int clientId) {
        for (ConnectedClient c : tcp.getClients().values()) {
            if (c == null) {
                continue;
            }
            if (c.getClientId() == clientId) {
                if (pressed) {
                    c.getInput().keyPressed(key);
                } else {
                    c.getInput().keyReleased(key);
                }
            }
        }
    }

    public String encryptData(String txt) {
        if ((txt != null) && (!txt.isEmpty())) {
            if (gc.getNetworkSecurity() != null) {
                return gc.getNetworkSecurity().encryptData(gc, txt);
            }
        }
        return txt;
    }

    public String decryptData(String txt) {
        if (txt != null && !txt.isEmpty()) {
            if (gc.getNetworkSecurity() != null) {
                return gc.getNetworkSecurity().decryptData(gc, txt);
            }
        }
        return txt;
    }

    public void addEventListener(ServerEventController e) {
        if (e == null) {
            return;
        }
        eventController = e;
    }

    public void executeCommand(String commande, int clientId) {
        boolean help = false;
        CommandType commandPrefix = Command.lookupForCommandType(commande);
        //If custom command
        if (commandPrefix == CommandType.INVALID) {
            if (eventController != null) {
                eventController.onCustomCommand(tcp.getClients().get((Integer) clientId), commande);
            }
            return;
        }

        //If help command
        if (commande.trim().toLowerCase().equals("help")) {
            new CommandHelp(gc).execute();
            return;
        }
        commande = commande.toLowerCase();
        writeConsole("Executing : " + commande);

        String arguments = "";
        try {
            arguments = commande.substring(commandPrefix.getPrefix().length() + 1);
            if (arguments.equals("help")) {
                help = true;
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            // No error, just command without arguments
        }

        switch (commandPrefix) {
            case INVALID:
                writeConsole("#oUnknown Command - Use <Cmd List> To Get Some Help");
                break;
            case MESSAGE_PLAYER:
                if (help) {
                    new CommandMessagePlayer(gc).help();
                } else {
                    new CommandMessagePlayer(gc, arguments.split(" ")[0], arguments.split(" ")[1]).execute();
                }
                break;
            case SERVER_ANNOUNCE:
                if (help) {
                    new CommandServerAnnounce(gc).help();
                } else {
                    new CommandServerAnnounce(gc, arguments).execute();
                }
                break;
            case HELP:
                new CommandHelp(gc).execute();
                break;
        }

    }

    public boolean switchServerState() {
        return isRunning() ? stopServer() : startServer();
    }

    public boolean isRunning() {
        return running;
    }

    public ConcurrentHashMap<Integer, ConnectedClient> getClients() {
        return tcp.getClients();
    }

    public ServerEventController getEventController() {
        return eventController;
    }

    public GameContainer getGc() {
        return gc;
    }

    public ServerConsole getConsole() {
        return console;
    }

    public long getCurrentPingMs() {
        return currentPingMs;
    }

}
