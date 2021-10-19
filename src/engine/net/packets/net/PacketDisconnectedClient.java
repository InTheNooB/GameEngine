package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

public class PacketDisconnectedClient extends Packet {

    private int clientId;

    public PacketDisconnectedClient() {
        super("PDN");
    }

    public PacketDisconnectedClient(int clientId) {
        super("PDN");
        this.clientId = clientId;
    }

    @Override
    public String getData() {
        return abv + clientId;
    }

    @Override
    public void processData(String input) {
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

    @Override
    public void processData(String input, Client c) {
    }
}
