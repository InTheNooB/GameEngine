package engine.net.server.commands;

import engine.GameContainer;
import engine.net.packets.net.PacketMessage;
import engine.net.server.Server;

public class CommandServerAnnounce extends Command {

    private String msg;

    public CommandServerAnnounce(GameContainer gc, String msg) {
        super("msg global", gc);
        this.msg = msg;
    }

    public CommandServerAnnounce(GameContainer gc) {
        super("msg global", gc);
    }

    @Override
    public void execute() {
        gc.getServer().writeConsole("<Server> " + msg);
        gc.getServer().sendTCPPacket(new PacketMessage("<Server> " + msg).getData());
    }

    @Override
    public void help() {
        gc.getServer().writeConsole("#c---<Msg Global>---");
        gc.getServer().writeConsole("Syntax : Msg Global <Text> ");
        gc.getServer().writeConsole("Definition : Sends a global message to each clients");
        gc.getServer().writeConsole("#c---<Msg Global>---");
    }
}
