package engine.net.client.events;

import engine.net.packets.gameObject.PacketAddGameObject;

public interface ClientEventController {

    public void onConnect();

    public void onDisconnect();

    public void onPacketAddGameObject(PacketAddGameObject packet);

    public void onCustomPacket(String abv, String data);

}
