/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.net.packets.gameObject;

import engine.GameContainer;
import engine.game.GameObject;
import engine.game.Orientation;
import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lione
 */
public class PacketAddGameObject extends Packet {

    private float x, y;
    private float w, h;
    private Orientation dir;
    private int netId;
    private String className;

    public PacketAddGameObject(GameObject go, String className) {
        super("AGO");
        x = go.getX();
        y = go.getY();
        w = go.getWidth();
        h = go.getHeight();
        dir = go.getOrientation();
        netId = go.getNetId();
        this.className = className;
    }

    public PacketAddGameObject(GameContainer gc) {
        super("AGO", gc);
    }

    @Override
    public String getData() {
        return abv + x + ";" + y + ";" + w + ";" + h + ";" + dir + ";" + netId + ";" + className;
    }

    @Override
    public void processData(String input) {
        String[] values = input.split(";");
        x = Float.parseFloat(values[0]);
        y = Float.parseFloat(values[1]);
        w = Float.parseFloat(values[2]);
        h = Float.parseFloat(values[3]);
        dir = values[4].equals("LEFT") ? Orientation.LEFT : Orientation.RIGHT;
        netId = Integer.parseInt(values[5]);
        className = values[6];

        //Event
        if (gc.getClient().getEventController() != null) {
            gc.getClient().getEventController().onPacketAddGameObject(this);
        }
    }

    @Override
    public void processData(String input, Client c) {
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    public float getH() {
        return h;
    }

    public void setH(float h) {
        this.h = h;
    }

    public Orientation getDir() {
        return dir;
    }

    public void setDir(Orientation dir) {
        this.dir = dir;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
