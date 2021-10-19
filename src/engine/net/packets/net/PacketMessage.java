package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

public class PacketMessage extends Packet {

    private String message;

    public PacketMessage(String message) {
        super("MSG");
        this.message = message;
    }

    @Override
    public String getData() {
        return abv + message;
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
