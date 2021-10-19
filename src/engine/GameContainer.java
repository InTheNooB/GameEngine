package engine;

import engine.input.Input;
import engine.consts.FileConstants;
import engine.consts.Settings;
import engine.debug.DateHandler;
import engine.debug.EventHistory;
import engine.game.GameObject;
import engine.game.GameObjectSelector;
import engine.game.particles.Particle;
import engine.ihm.camera.Camera;
import engine.ihm.Window;
import engine.io.FileHandler;
import engine.net.NetType;
import static engine.net.NetType.*;
import engine.net.client.Client;
import engine.net.packets.net.PacketPingMs;
import engine.net.server.Server;
import engine.security.NetworkSecurity;
import engine.security.NetworkSecurityType;
import java.io.File;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameEngine 2D par Lionel Ding. Points utiles : Chaque objet peut être défini
 * comme "GameObject", les GameObject possèdes divers atributs/méthode de bases
 * dont "Setup", "SetupAnimation", "Update", "Render" qui sont executés (par
 * défaut) automatiquement, toutes les frames pour le render et l'update, et une
 * fois pour les setups. Les méthodes updates toutes les frames peuvent être
 * ignoré avec les attributs "updated" et "rendered". Egalement, ces GameObjects
 * peuvent être sélectionné afin d'afficher certaines de leurs informations. Ces
 * GameObject possède des méthodes qui permettent de les render en prenant en
 * compte le scale et les offsets : "getRealPos", "getRealWidth",
 * "getRealHeight". Ils possèdent un Sprite et un AnimationSet, pour render le
 * sprite : "drawSprite", pour l'animation : "drawAnimation".
 *
 * Les paramètres généraux se trouvent dans la classe Settings, soit
 * "gc.getSettings().XXX", pour ce qui est des paramètres de la camera, ils se
 * trouvent dans la classe Camera, soit "gc.getCam().XXX".
 *
 *
 *
 * @author lione
 */
public class GameContainer implements Runnable, FileConstants {

    //GLOBAL
    private Thread thread;
    private Settings settings;
    private EventHistory eventHistory;

    //IHM
    private Window window;
    private Camera cam;

    //INPUTS
    private Input input;

    //GAME
    private final AbstractGame game;
    private GameObjectSelector gameObjectSelector;
    private CopyOnWriteArrayList<GameObject> gameObjects;
    private ArrayList<Particle> particles;

    //NET
    private Server server;
    private Client client;
    private NetworkSecurity networkSecurity;
    private int netIdCompteur;

    //MAIN LOOP
    private boolean running = true;
    private double startTime;
    private int maxFps = 100; // FPS
    private int maxUps = 60; // UPS

    public GameContainer(AbstractGame game) {
        this.game = game;
    }

    public void start() {
        createFolders();
        settings = new Settings(this);
        window = new Window(this);
        input = new Input(this);
        netIdCompteur = 0;
        eventHistory = new EventHistory();
        gameObjectSelector = new GameObjectSelector(this);
        gameObjects = new CopyOnWriteArrayList();
        particles = new ArrayList();
        thread = new Thread(this);
        cam = new Camera(this);
        game.setup(this);
        // needs to be set visible after the game is setup because of the "setFullscreen" which must be called before the "setvisible"
        window.getFrame().setVisible(true);
        // needs to be created after the game is setup because of the offset which depends on the screen size, which can change in the setup
        cam.updateScreenSize(this);
        thread.start();
    }

    public void stop() {
        endProgram();
    }

    @Override
    public void run() {

        final double uOPTIMAL_TIME = 1000000000 / maxUps;
        final double fOPTIMAL_TIME = 1000000000 / maxFps;

        double uDeltaTime = 0, fDeltaTime = 0;
        int frames = 0, updates = 0;
        startTime = System.nanoTime();
        long secondTimer = System.currentTimeMillis();
        long threeSecondsTimer = System.currentTimeMillis();

        while (running) {

            // Calculate difference in time
            long currentTime = System.nanoTime();
            long currentTimeMillis = System.currentTimeMillis();

            uDeltaTime += (currentTime - startTime);
            fDeltaTime += (currentTime - startTime);
            startTime = currentTime;

            if (uDeltaTime >= uOPTIMAL_TIME) {
                // <UPDATE>
                updateGame((float) (uDeltaTime / 1000000000));
                // </UPDATE>
                uDeltaTime -= uOPTIMAL_TIME;
                updates++;
            }
            if (fDeltaTime >= fOPTIMAL_TIME) {
                // <RENDER>
                if (game.isRendered()) {
                    window.render();
                }
                // </RENDER>
                fDeltaTime -= fOPTIMAL_TIME;
                frames++;
            }

            if (currentTimeMillis - secondTimer >= 1000) {
                secondTimer += 1000;
                // Ran Every seconds 
                String ping = ((client != null) && (client.isConnected())) ? " - Ping : " + client.getCurrentPingMs() + " " : "";
                if (settings.getTitle().startsWith("FPS : ") || settings.getTitle().startsWith("Game Engine")) {
                    // Prevent from overriding a title
                    if (ping.equals("")) {
                        settings.setTitle("UPS : " + updates + " -  Game Engine " + settings.getGameVersion());
                    } else {
                        settings.setTitle("UPS : " + updates + ping + " -  Game Engine " + settings.getGameVersion());
                    }
                }

                updates = 0;
                frames = 0;
            }
            if (currentTimeMillis - threeSecondsTimer >= 3000) {
                threeSecondsTimer += 3000;
                // Ran Every 3 seconds 
                //Update the client (ping)
                if ((client != null) && (client.isConnected())) {
                    client.sendTCPPacket(new PacketPingMs(currentTimeMillis).getData());
                }
            }
        }
    }

    private void updateGame(float dTime) {

        //Update the window (Buttons,...)
        window.update(this);

        //Update game
        if (game.isUpdated()) {
            game.update(this, (float) dTime);
        }
        //Remove dead gameobject / Update each object
        for (int i = 0; i < gameObjects.size(); i++) {
            if (!gameObjects.get(i).isAlive()) {
                gameObjects.remove(i);
                i--;
            } else {
                if (gameObjects.get(i).isUpdated()) {
                    gameObjects.get(i).update(this, (float) dTime);
                }
            }
        }

        //Remove dead particles and updates them
        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            if ((int) getRunningTime() - p.getCreationTime() > p.getTtl()) {
                p.setDead(true);
            }
            if (p.isUpdated()) {
                p.update(this);
            }
            if (p.isDead()) {
                particles.remove(i);
                i--;
            }
        }
        //Update cam
        cam.update(this);

        //Update the GameObjectSelector
        gameObjectSelector.update(this);

        //Update the inputs
        input.update();
    }

    public double getRunningTime() {
        return System.nanoTime() / 1000000000.0 - startTime;
    }

    /**
     * Adds a GameObject to the GameEngine which will be updated and redered if
     * defined so.
     *
     * @param go
     */
    public void addGameObject(GameObject go) {
        if (go != null) {
            gameObjects.add(go);
            go.setNetId(netIdCompteur);
            netIdCompteur++;
            go.setup(this);
            go.setupAnimation(this);
        } else {
            eventHistory.addEvent("NullPointer On addGameObject");
        }
    }

    public void removeGameObject(GameObject go) {
        if (go != null) {
            gameObjects.remove(go);
        } else {
            eventHistory.addEvent("NullPointer On removeGameObject");
        }
    }

    /**
     * Adds a GameObject to the GameEngine which will be updated and redered if
     * defined so.
     *
     * @param p
     */
    public void addParticle(Particle p) {
        if (p != null) {
            particles.add(p);
            p.setup(this);
        } else {
            eventHistory.addEvent("NullPointer On addParticle");
        }
    }

    public void removeParticle(Particle p) {
        if (p != null) {
            particles.remove(p);
        } else {
            eventHistory.addEvent("NullPointer On removeParticle");
        }
    }

    public void enableNetwork(NetType type) {
        switch (type) {
            case CLIENT:
                client = new Client(this);
                break;
            case SERVER: {
                try {
                    server = new Server(this);
                } catch (SocketException ex) {
                    eventHistory.addEvent("Error while creating the server");
                }
            }
            break;
        }
    }

    public void disableNetwork(NetType type) {
        switch (type) {
            case CLIENT:
                if (client != null) {
                    if (client.isConnected()) {
                        client.disconnect();
                    }
                    client = null;
                }
                break;
            case SERVER: {
                if (server != null) {
                    if (server.isRunning()) {
                        server.stopServer();
                    }
                    server = null;
                }
            }
            break;
        }
    }

    public void stopServer() {
        if ((server != null) && server.isRunning()) {
            server.stopServer();
        }
    }

    public void stopClient() {
        if ((client != null) && client.isConnected()) {
            client.disconnect();
        }
    }

    /**
     * Ends the program and write logs into the log files
     */
    public void endProgram() {
        running = false;
        stopServer();
        stopClient();
        eventHistory.showEvents();
        List<String> events;
        events = new CopyOnWriteArrayList();
        events.add(DateHandler.getDate() + " : ");
        events.addAll(eventHistory.getEvents());
        for (String e : events) {
            FileHandler.appendToTextFile(FOLDER_LOGS + SPR + "log.txt", e);
        }
        System.exit(0);
    }

    private void createFolders() {
        new File("." + SPR + FOLDER_ASSETS).mkdir();
        new File("." + SPR + FOLDER_ANIMATIONS).mkdir();
        new File("." + SPR + FOLDER_FONTS).mkdir();
        new File("." + SPR + FOLDER_SOUNDS).mkdir();
        new File("." + SPR + FOLDER_IMAGES).mkdir();
        new File("." + SPR + FOLDER_LOGS).mkdir();
        new File("." + SPR + FOLDER_MAPS).mkdir();
    }

    /**
     * Changes the max Updates Per Seconds. (Default is 60)
     *
     * @param maxUps
     */
    public void setMaxUps(int maxUps) {
        this.maxUps = maxUps;
    }

    /**
     * Changes the max Frames Per Seconds. (Default is 100)
     *
     * @param maxFps
     */
    public void setMaxFps(int maxFps) {
        this.maxFps = maxFps;
    }

    public Window getWindow() {
        return window;
    }

    public Input getInput() {
        return input;
    }

    public CopyOnWriteArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public AbstractGame getGame() {
        return game;
    }

    public Camera getCam() {
        return cam;
    }

    public Settings getSettings() {
        return settings;
    }

    public EventHistory getEventHistory() {
        return eventHistory;
    }

    public GameObjectSelector getGameObjectSelector() {
        return gameObjectSelector;
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> particles) {
        this.particles = particles;
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public void enableNetworkSecurity(NetworkSecurityType securityType) {
        networkSecurity = new NetworkSecurity(securityType);
    }

    public NetworkSecurity getNetworkSecurity() {
        return networkSecurity;
    }

    public int getMaxFps() {
        return maxFps;
    }

    public int getMaxUps() {
        return maxUps;
    }

}
