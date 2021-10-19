package engine.game.animation;

import java.io.Serializable;

public class Timer implements Serializable {

    private long startTime;
    private static long currentTimePassed;

    public Timer() {
        startTime = System.currentTimeMillis();
    }

    public void update() {
        currentTimePassed = System.currentTimeMillis() - startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public static long getCurrentTimePassed() {
        return currentTimePassed;
    }

    public void setCurrentTimePassed(long currentTimePassed) {
        this.currentTimePassed = currentTimePassed;
    }

}
