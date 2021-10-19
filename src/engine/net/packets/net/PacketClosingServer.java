package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

public class PacketClosingServer extends Packet {

    public PacketClosingServer() {
        super("CSV");
    }

    @Override
    public String getData() {
        return abv;
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
