package engine.net.server.events;

import engine.net.server.ConnectedClient;

public interface ServerEventController {

    public void onUserConnect(ConnectedClient client);

    public void onUserDisconnect(ConnectedClient client);

    public void onCustomPacket(ConnectedClient c, String abv, String data);

    public void onCustomCommand(ConnectedClient c, String commande);

}
