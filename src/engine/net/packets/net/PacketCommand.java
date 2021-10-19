package engine.net.packets.net;

import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;
import engine.net.server.Server;

public class PacketCommand extends Packet {

    private String command;
    private int clientId;

    public PacketCommand(Server server) {
        super("", server);
    }

    public PacketCommand(String command, int clientId) {
        super("CMD");
        this.command = command;
        this.clientId = clientId;
    }

    @Override
    public String getData() {
        return abv + command.substring(1) + ";" + clientId;
    }

    @Override
    public void processData(String input) {
        command = input.split(";")[0];
        clientId = Integer.parseInt(input.split(";")[1]);
        server.executeCommand(command, clientId);
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

    @Override
    public void processData(String input, Client c) {
    }

}
