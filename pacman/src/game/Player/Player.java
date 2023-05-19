package game.Player;

import ch.aplu.jgamegrid.Location;
import game.CharacterType;
import game.Items.Item;
import game.Items.Pill;
import game.Level;
import game.MovingActor;

/**
 * The PacMan.
 */

public abstract class Player extends MovingActor {
    private int idSprite = 0;
    private int nbPills = 0;
    private int score = 0;

    /**
     * Creates a moving actor based on one or more sprite images.
     *
     * @param isRotatable : if true, the actor's image may be rotated when the direction changes
     * @param nbSprites   : the number of sprite images for the same actor
     * @param seed        : the seed for random behaviors of the actor
     * @param type
     */
    public Player(boolean isRotatable, int nbSprites, int seed, CharacterType type) {
        super(isRotatable, nbSprites, seed, type);
    }

    /**
     * Called in every simulation iteration.
     */
    @Override
    public void act() {
        updateSprite();
        super.act();
        ((Level) gameGrid).getGameCallback()
                         .pacManLocationChanged(getLocation(), score, nbPills);
    }

    /**
     * Moves to one of the 8 neighbour cells in the current direction,
     * and checks if anything could be eaten there.
     */
    @Override
    public synchronized void move() {
        if (!shouldMove()) return;  // check for this simulation iteration
        resetMove();  // reset for the next simulation iteration

        // move and eat
        addVisitedList(getNextMoveLocation());
        super.move();
        eatItem(getLocation());
    }

    /**
     * Tries to eat an item at the given location.
     * Correspondingly, updates any change in status to the game.
     * @param location: location to be checked
     */
    private void eatItem(Location location) {
        Level level = (Level) gameGrid;
        Item item = level.getSettingManager().getItem(location);
        if (item == null || !item.isEatable()) // no item here
            return;

        // update pills count and score
        updateStatus(item);

        level.getGameCallback().pacManEatPillsAndItems(location, item.getName());
        level.getSettingManager().removeItem(location);
    }

    /**
     * Updates the PacActor's internal and displayed status
     * (i.e. number of eaten pills and score) according to the item's effect.
     * @param item: item that may affect the PacActor's status
     */
    private void updateStatus(Item item) {
        if (item instanceof Pill pill) {
            nbPills++;
            score += pill.getScoreEffect();
        }

        String title = "[PacMan in the Multiverse] Current score: " + score;
        gameGrid.setTitle(title);
    }


    /**
     * Gets the number of pills the PacActor has eaten.
     * @return the number of pills eaten.
     */
    public int getNbPills() {
        return nbPills;
    }

    /**
     * Updates the sprite for the PacActor.
     */
    private void updateSprite() {
        show(idSprite);
        idSprite++;
        if (idSprite == PlayerFactory.NB_SPRITES)
            idSprite = 0;
    }

    protected abstract boolean shouldMove();

    protected abstract void resetMove();
}
