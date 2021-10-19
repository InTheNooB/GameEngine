package engine.net.packets.net;

import engine.GameContainer;
import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

public class PacketPingMs extends Packet {

    private long time;

    public PacketPingMs(long time) {
        super("PIM");
        this.time = time;
    }

    public PacketPingMs(GameContainer gc) {
        super("PIM", gc);
    }
    public PacketPingMs(String input) {
        super("PIM");
        time = Long.parseLong(input);
    }

    @Override
    public String getData() {
        return abv + time;
    }

    @Override
    public void processData(String input) {
        time = Long.parseLong(input);
        long delay = System.currentTimeMillis() - time;
        time = delay;
        gc.getClient().setCurrentPingMs(delay);
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

    @Override
    public void processData(String input, Client c) {
    }

}
