package engine.net.packets.inputs;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;
import engine.net.server.Server;

public class PacketInput extends Packet {

    private int key;
    private boolean pressed;

    public PacketInput(Server server) {
        super("PIN", server);
    }

    public PacketInput() {
         super("PIN");
    }

    @Override
    public String getData() {
        return abv + key + ";" + pressed;
    }

    @Override
    public void processData(String input) {
    }

    @Override
    public void processData(String input, Client c) {
    }

    public void processData(String input, int clientId) {
        String[] array = input.split(";");
        if (array.length != 2) {
            return;
        } else if ("".equals(array[0])) {
            return;
        }
        key =  Integer.parseInt(input.split(";")[0]);
        pressed = input.split(";")[1].equals("true");
        server.processReceivedInput(key,pressed,clientId);
    }

    public void update(int key, boolean pressed) {
        this.key = key;
        this.pressed = pressed;
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

}
