package engine.net.server.commands;

import engine.GameContainer;

public class CommandHelp extends Command {

    public CommandHelp(GameContainer gc) {
        super("cmd list", gc);
    }

    @Override
    public void execute() {
        gc.getServer().writeConsole("#c---<List Of Commands>---");
        for (CommandType ct : CommandType.values()) {
            gc.getServer().writeConsole("-> " + ct.getPrefix());
        }
        gc.getServer().writeConsole("#c---<List Of Commands>---");
    }

    @Override
    public void help() {
    }

}
