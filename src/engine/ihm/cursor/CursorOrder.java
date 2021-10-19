package engine.ihm.cursor;

public enum CursorOrder {
    OVERRIDE_CURRENT(0),
    BUTTON_HOVER(10),
    GAME_OBJECT_MOVE(12),
    GAME_OBJECT_HOVER(13);

    private CursorOrder(int weight) {
        this.weight = weight;
    }
    private int weight;

    public int getWeight() {
        return weight;
    }

}
