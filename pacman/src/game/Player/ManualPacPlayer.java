package game.Player;

import ch.aplu.jgamegrid.GGKeyRepeatListener;
import ch.aplu.jgamegrid.Location;

import java.awt.event.KeyEvent;


/**
 * The actual pacPlayer that response to user interaction.
 */
public class ManualPacPlayer extends PacPlayer implements GGKeyRepeatListener {

    /**
     * Creates a manual PacPlayer based on one or more sprite images.
     *
     * @param isRotatable : if true, the actor's image may be rotated when the direction changes
     * @param nbSprites   : the number of sprite images for the same actor
     * @param seed        : the seed for random behaviors of the actor
     */
    protected ManualPacPlayer(boolean isRotatable, int nbSprites, int seed) {
        super(isRotatable, nbSprites, seed);
    }

    /**
     * Event callback when the key is continuously pressed.
     * @param keyCode: the key code of the key
     */
    @Override
    public void keyRepeated(int keyCode) {
        if (isRemoved())  return; // pacActor removed
        resetMove();

        // Set the direction according to the key code
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> setDirection(Location.WEST);
            case KeyEvent.VK_UP -> setDirection(Location.NORTH);
            case KeyEvent.VK_RIGHT -> setDirection(Location.EAST);
            case KeyEvent.VK_DOWN -> setDirection(Location.SOUTH);
            default -> { return; }
        }

        if (!isMoveValid())  return;
        setShouldMove(true);
    }

    /** {@inheritDoc} */
    @Override
    protected void setNextDirection() {
        // nothing, already set.
    }
}
