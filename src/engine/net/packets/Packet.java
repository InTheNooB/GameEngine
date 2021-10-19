package engine.net.packets;

import engine.GameContainer;
import engine.consts.NetworkConstants;
import engine.net.client.Client;
import engine.net.server.ConnectedClient;
import engine.net.server.Server;

public abstract class Packet implements NetworkConstants {

    public enum PacketTypes {

        INVALID("INV"),
        CONNECTING("CON"),
        CLIENTID("CID"),
        CLOSING_SERVER("CSV"),
        MESSAGE("MSG"),
        COMMAND("CMD"),
        INPUT("PIN"),
        PING_MS("PIM"),
        DISCONNECTING("PDN"),
        SERVER_NAME("SVN"),
        ADD_GAME_OBJECT("AGO"),
        UPDATE_GAME_OBJECT("UGO"),
        REMOVE_GAME_OBJECT("RGO"),
        BROADCAST_LOOKUP_SERVER("BLS");

        private final String abv;

        private PacketTypes(String abv) {
            this.abv = abv;
        }

        public String getAbv() {
            return abv;
        }

    }

    protected String abv;
    protected Server server;
    protected GameContainer gc;

    public Packet(String abv, GameContainer gc) {
        this.abv = abv;
        this.gc = gc;
    }

    public Packet(String abv, Server server) {
        this.abv = abv;
        this.server = server;
    }

    public Packet(String abv) {
        this.abv = abv;
    }

    public String getAbv() {
        return abv;
    }

    public abstract String getData();

    public abstract void processData(String input);

    public abstract void processData(String input, ConnectedClient c);

    public abstract void processData(String input, Client c);

    public void update() {

    }
}
