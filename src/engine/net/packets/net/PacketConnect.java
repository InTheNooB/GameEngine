package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;
import engine.net.server.Server;

/**
 *
 * @author dingl01
 */
public class PacketConnect extends Packet {

    private String gameVersion;
    private int clientId;

    public PacketConnect(Server server, String gameVersion) {
        super("CON", server);
        this.gameVersion = gameVersion;
    }

    public PacketConnect(String gameVersion) {
        super("CON");
        this.gameVersion = gameVersion;
    }

    public PacketConnect(Server server, int clientId) {
        super("CON", server);
        this.clientId = clientId;
    }

    @Override
    public String getData() {
        return abv + gameVersion;
    }

    @Override
    public void processData(String input) {
        gameVersion = input;
        server.initNewClient(gameVersion, clientId);
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

    @Override
    public void processData(String input, Client c) {
    }
}
