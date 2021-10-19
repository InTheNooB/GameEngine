package engine.game;

import engine.GameContainer;
import engine.game.physics.Physics;
import engine.game.physics.Point;
import engine.ihm.cursor.CursorOrder;
import engine.ihm.game.Button;
import java.awt.Cursor;
import engine.ihm.game.ButtonEvent;

public class GameObjectSelector {

    private GameObject selectedObject;
    private GameObject heldObject;
    private Button followButton;
    private Button moveButton;

    public GameObjectSelector(GameContainer gc) {
        selectedObject = null;
        int ratio = (int) (gc.getSettings().getWidth() / 5);

        followButton = new Button((gc.getSettings().getWidth() - ratio + ratio / 5) - 1, 235, "Follow");
        followButton.setVisible(false);
        followButton.addListener(new ButtonEvent() {
            @Override
            public void onClick(GameContainer gc) {
                if (selectedObject != null) {
                    gc.getCam().setxFocus((gc.getCam().getxFocus() == null) ? selectedObject : null);
                    gc.getCam().setyFocus((gc.getCam().getyFocus() == null) ? selectedObject : null);
                }
            }

            @Override
            public void onHover(GameContainer gc) {
            }
        });

        moveButton = new Button((gc.getSettings().getWidth() - ratio + ratio / 5) - 1, 260, "Move");
        moveButton.setVisible(false);
        moveButton.addListener(new ButtonEvent() {
            @Override
            public void onClick(GameContainer gc) {
                if (selectedObject != null) {
                    if (heldObject == null) {
                        gc.getCam().setFocus(null);
                        heldObject = selectedObject;
                    } else {
                        heldObject = null;
                    }
                }
            }

            @Override
            public void onHover(GameContainer gc) {
            }
        });

        gc.getWindow().addButton(followButton);
        gc.getWindow().addButton(moveButton);
    }

    public void update(GameContainer gc) {

        float mX = gc.getInput().getMouseX();
        float mY = gc.getInput().getMouseY();

        float scale = gc.getCam().getScale();
        Point offSet = gc.getCam().getOffSet();

        boolean flagSelection = false;
        int ratio = (int) (gc.getSettings().getWidth() / 5);

        if (mX >= (gc.getSettings().getWidth() - ratio + ratio / 5) - 1) {
            if (mY < 270) {
                return;
            }
        }

        //Check for selected gameobject
        for (GameObject go : gc.getGameObjects()) {
            if (go.isSelectable()) {
                if (Physics.collision(new Point(mX, mY), go.getRealPos(gc).x, go.getRealPos(gc).y, go.getRealWidth(gc), go.getRealHeight(gc))) {
                    if (heldObject == null) {
                        gc.getWindow().getCursorManager().setCursor(Cursor.CROSSHAIR_CURSOR, CursorOrder.GAME_OBJECT_HOVER);
                    }
                    if (gc.getInput().isButtonDown(1)) {
                        // Pressed
                        if (selectedObject == go) {
                            selectedObject = null;
                            heldObject = null;
                            followButton.setVisible(false);
                            moveButton.setVisible(false);
                        } else {
                            selectedObject = go;
                            followButton.setVisible(true);
                            moveButton.setVisible(true);
                        }
                        flagSelection = true;
                        break;
                    }
                }
            }
        }
        if (gc.getInput().isButtonDown(1)) {
            if (!flagSelection) {
                selectedObject = null;
                heldObject = null;
                followButton.setVisible(false);
                moveButton.setVisible(false);
            }
        }

        //Moves held object
        if (heldObject != null) {
            gc.getWindow().getCursorManager().setCursor(Cursor.MOVE_CURSOR, CursorOrder.GAME_OBJECT_MOVE);

            heldObject.setX((mX - gc.getSettings().getWidth() / 2) / scale - offSet.x - heldObject.getWidth() / 2);
            heldObject.setY((mY - gc.getSettings().getHeight() / 2) / scale - offSet.y - heldObject.getHeight() / 2);
        }
    }

    public GameObject getSelected() {
        return selectedObject;
    }

    public Button getFollowButton() {
        return followButton;
    }

    public void setFollowButton(Button followButton) {
        this.followButton = followButton;
    }

    public Button getMoveButton() {
        return moveButton;
    }

    public void setMoveButton(Button moveButton) {
        this.moveButton = moveButton;
    }

}
