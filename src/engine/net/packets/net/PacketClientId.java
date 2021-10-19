package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

public class PacketClientId extends Packet {

    private int clientId;

    public PacketClientId() {
        super("CID");
    }

    public PacketClientId(int clientId) {
        super("CID");
        this.clientId = clientId;
    }

    @Override
    public String getData() {
        return abv + clientId;
    }

    @Override
    public void processData(String input, ConnectedClient c) {
        c.setClientId(Integer.parseInt(input));
    }

    public void processData(String input, Client c) {
        c.setClientId(Integer.parseInt(input));
    }

    @Override
    public void processData(String input) {

    }

}
