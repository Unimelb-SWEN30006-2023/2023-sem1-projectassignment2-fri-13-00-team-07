package game;

import ch.aplu.jgamegrid.*;
import game.Items.Item;
import game.Items.Pill;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The PacMan.
 */

public class PacActor extends MovingActor implements GGKeyRepeatListener {
    private static final int NB_SPRITES = 4;
    private int idSprite = 0;
    private int nbPills = 0;
    private int score = 0;
    private ArrayList<String> propertyMoves;
    private boolean isAuto = false;

    // whether the pacActor can move in this simulation iteration
    private boolean shouldMove = false;

    /**
     * Creates a PacActor.
     * @param seed: the seed for random behaviors of the PacActor.
     */
    public PacActor(int seed) {
        super(true, NB_SPRITES, seed, CharacterType.PACMAN);
    }

    /**
     * Sets the PacActor to auto mode.
     * @param auto: whether the PacActor is to be in auto mode
     */
    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    /**
     * Sets the moves (from the properties file) for the PacActor.
     * @param propertyMoves: ArrayList of Strings, representing the property moves.
     */
    public void setPropertyMoves(ArrayList<String> propertyMoves) {
        this.propertyMoves = propertyMoves;
    }

    /**
     * Event callback when the key is continuously pressed.
     * @param keyCode: the key code of the key
     */
    @Override
    public void keyRepeated(int keyCode) {
        if (isAuto)  return; // should not use in auto mode
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
        if (!shouldMove) return;  // check for this simulation iteration
        shouldMove = false;  // reset for the next simulation iteration

        // move and eat
        addVisitedList(getNextMoveLocation());
        super.move();
        eatItem(getLocation());
    }

    /**
     * Sets the next (valid) direction.
     * Only used when moving in auto mode.
     */
    @Override
    protected void setNextDirection() {
        if (!isAuto) // direction already set in keyboard response
            return;

        if (!propertyMoves.isEmpty()) {
            followPropertyMoves();
            return;
        }

        // at this stage: either go to a valid pill, or move randomly
        shouldMove = true;
        double oldDirection = getDirection();

        // walk towards the closest item if it can
        Location closestItem = ((Level) gameGrid).getSettingManager().closestItemLocation(getLocation());
        setDirectionToTarget(closestItem);
        if (isMoveValid() && !isVisited(getNextMoveLocation()))
            return;

        // last resort: random walk
        setRandomMoveDirection(oldDirection);
    }

    /**
     * Sets the direction according to moves from the properties file.
     */
    private void followPropertyMoves() {
        String currentMove = propertyMoves.remove(0);
        switch (currentMove) {
            case "R" -> turn(90);
            case "L" -> turn(-90);
            case "M" -> {
                if (isMoveValid()) {
                    shouldMove = true;
                }
            }
        }
    }

    /**
     * Tries to eat an item at the given location.
     * Correspondingly, updates any change in status to the game.
     * @param location: location to be checked
     */
    private void eatItem(Location location) {
        Level level = (Level) gameGrid;
        Item item = level.getSettingManager().getItem(location);
        if (item == null) // no item here
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
        if (idSprite == NB_SPRITES)
            idSprite = 0;
    }
}
