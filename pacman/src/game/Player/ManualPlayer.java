package game.Player;

import ch.aplu.jgamegrid.GGKeyRepeatListener;
import ch.aplu.jgamegrid.Location;
import game.CharacterType;

import java.awt.event.KeyEvent;

public class ManualPlayer extends Player implements GGKeyRepeatListener {

    // whether the pacActor can move in this simulation iteration
    private boolean shouldMove = false;

    /**
     * Creates a moving actor based on one or more sprite images.
     *
     * @param isRotatable : if true, the actor's image may be rotated when the direction changes
     * @param nbSprites   : the number of sprite images for the same actor
     * @param seed        : the seed for random behaviors of the actor
     * @param type
     */
    public ManualPlayer(boolean isRotatable, int nbSprites, int seed, CharacterType type) {
        super(isRotatable, nbSprites, seed, type);
    }

    /**
     * Event callback when the key is continuously pressed.
     * @param keyCode: the key code of the key
     */
    @Override
    public void keyRepeated(int keyCode) {
        if (isRemoved())  return; // pacActor removed
        shouldMove = false;

        // Set the direction according to the key code
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> setDirection(Location.WEST);
            case KeyEvent.VK_UP -> setDirection(Location.NORTH);
            case KeyEvent.VK_RIGHT -> setDirection(Location.EAST);
            case KeyEvent.VK_DOWN -> setDirection(Location.SOUTH);
            default -> { return; }
        }

        if (!isMoveValid())  return;
        shouldMove = true;
    }

    @Override
    protected void setNextDirection() {
        // nothing, already set.
    }

    @Override
    protected boolean shouldMove() {
        return shouldMove;
    }

    @Override
    protected void resetMove() {
        shouldMove = false;
    }
}
