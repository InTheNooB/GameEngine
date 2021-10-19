package engine.net.server.inputProcessing;

import engine.GameContainer;

public class InputProcessingThread implements Runnable {

    private final GameContainer gc;
    private final String input;
    private final int clientId;

    public InputProcessingThread(String input, int clientId, GameContainer gc) {
        this.gc = gc;
        this.input = input;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        gc.getServer().processPacket(input, clientId);
    }

}
