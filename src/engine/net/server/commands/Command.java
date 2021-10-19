/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.net.server.commands;

import engine.GameContainer;

/**
 *
 * @author lione
 */
public abstract class Command {

    public enum CommandType {
        INVALID("-"),
        MESSAGE_PLAYER("msg player"),
        HELP("cmd list"),
        SERVER_ANNOUNCE("msg global");

        private CommandType(String prefix) {
            this.prefix = prefix;
        }

        private String prefix;

        public String getPrefix() {
            return prefix;
        }

    }
    protected String prefix;
    protected boolean error;
    protected GameContainer gc;

    public Command(String prefix) {
        this.prefix = prefix;
    }

    public Command(String prefix, GameContainer gc) {
        this.prefix = prefix;
        this.gc = gc;
    }

    /**
     * Finds the corresponding CommandType using it's prefix
     *
     * @param input The full command
     * @return The corresponding CommandType
     */
    public static CommandType lookupForCommandType(String input) {
        try {
            String command = input.split(" ")[0] + " " + input.split(" ")[1];
            for (CommandType ct : CommandType.values()) {
                if (ct.getPrefix().equals(command)) {
                    return ct;
                }
            }
        } catch (Exception e) {
        }
        return CommandType.INVALID;
    }

    public abstract void execute();

    public abstract void help();
}
