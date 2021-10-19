package engine.net.packets.gameObject;

import engine.GameContainer;
import engine.game.GameObject;
import engine.game.Orientation;
import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

public class PacketUpdateGameObject extends Packet {

    private float x, y;
    private Orientation dir;
    private float velX, velY;
    private int netId;
    private boolean alive;

    public PacketUpdateGameObject(GameObject go) {
        super("UGO");
        x = go.getX();
        y = go.getY();
        velX = go.getVelX();
        velY = go.getVelY();
        dir = go.getOrientation();
        netId = go.getNetId();
        alive = go.isAlive();
    }

    public PacketUpdateGameObject(GameContainer gc) {
        super("UGO", gc);
    }

    @Override
    public String getData() {
        return abv + x + ";" + y + ";" + velX + ";" + velY + ";" + dir + ";" + netId + ";" + alive;
    }

    @Override
    public void processData(String input) {
        String[] values = input.split(";");
        x = Float.parseFloat(values[0]);
        y = Float.parseFloat(values[1]);
        velX = Float.parseFloat(values[2]);
        velY = Float.parseFloat(values[3]);
        dir = values[4].equals("LEFT") ? Orientation.LEFT : Orientation.RIGHT;
        netId = Integer.parseInt(values[5]);
        alive = values[6].equals("true");
        
        for (GameObject gameObject : gc.getGameObjects()) {
            if (gameObject.getNetId() == netId) {
                gameObject.setX(x);
                gameObject.setY(y);
                gameObject.setVelX(velX);
                gameObject.setVelY(velY);
                gameObject.setOrientation(dir);
                gameObject.setAlive(alive);
                gameObject.setX(x);
                break;
            }
        }
    }

    @Override
    public void processData(String input, Client c) {
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

}
