/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.net.packets.gameObject;

import engine.GameContainer;
import engine.game.GameObject;
import engine.net.client.Client;
import engine.net.packets.Packet;
import engine.net.server.ConnectedClient;

/**
 *
 * @author lione
 */
public class PacketRemoveGameObject extends Packet {

    private int netId;

    public PacketRemoveGameObject(GameObject go) {
        super("RGO");
        netId = go.getNetId();
    }

    public PacketRemoveGameObject(GameContainer gc) {
        super("RGO", gc);
    }

    @Override
    public String getData() {
        return abv + netId;
    }

    @Override
    public void processData(String input) {
        netId = Integer.parseInt(input);
        GameObject toRemove = null;
        for (GameObject gameObject : gc.getGameObjects()) {
            if (gameObject.getNetId() == netId) {
                toRemove = gameObject;
            }
        }
        if (toRemove != null) {
            gc.removeGameObject(toRemove);
        }
    }

    @Override
    public void processData(String input, Client c) {
    }

    @Override
    public void processData(String input, ConnectedClient c) {
    }

}
