package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;
import engine.net.server.Server;

public class PacketServerName extends Packet {

    public PacketServerName(Server server) {
        super("SVN", server);
    }

    @Override
    public String getData() {
        if (server != null) {
            return abv + server.getGc().getSettings().getServerName() + ";" + server.getGc().getSettings().getGameVersion();
        } else {
            return abv;
        }
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
