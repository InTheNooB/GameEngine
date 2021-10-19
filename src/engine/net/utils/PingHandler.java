package engine.net.utils;

import engine.GameContainer;
import static engine.consts.NetworkConstants.PING_DELAY;

public class PingHandler extends Thread {

    private volatile boolean running;
    private GameContainer gc;

    public PingHandler(GameContainer gc) {
        super("Ping Handler Thread");
    }

    @Override
    public void run() {
        running = true;
        int lastPingSentTime = 0;
        while (running) {
            if (System.currentTimeMillis() - lastPingSentTime >= PING_DELAY) {
                
            }
            /*
            }
            
            */
        }
    }
}
