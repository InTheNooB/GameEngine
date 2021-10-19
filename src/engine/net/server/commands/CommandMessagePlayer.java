package engine.net.server.commands;

import engine.GameContainer;
import engine.net.packets.net.PacketMessage;
import engine.net.server.Server;

public class CommandMessagePlayer extends Command {

    private String msg;
    private int clientId;

    public CommandMessagePlayer(GameContainer gc, String clientId, String msg) {
        super("msg player", gc);
        this.msg = msg;
        try {
            this.clientId = Integer.parseInt(clientId);
        } catch (java.lang.NumberFormatException e) {
            gc.getServer().writeConsole("#oWrong Syntax <" + clientId + ">");
        }
    }

    public CommandMessagePlayer(GameContainer gc) {
        super("msg player", gc);
    }

    @Override
    public void execute() {
        gc.getServer().sendTCPPacket(gc.getServer().getClients().get((Integer) clientId), new PacketMessage("<Server (Private)> " + msg).getData());
        gc.getServer().writeConsole("<Server (" + clientId + ")> " + msg);
    }

    @Override
    public void help() {
        gc.getServer().writeConsole("#c---<Msg Player>---");
        gc.getServer().writeConsole("Syntax : Msg Player <ClientId> <Text>");
        gc.getServer().writeConsole("Definition : Sends a private message to a specific player");
        gc.getServer().writeConsole("#c---<Msg Player>---");
    }
}
