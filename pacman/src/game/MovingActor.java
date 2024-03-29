package game;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import game.Items.Item;
import game.Items.Portal;

import java.util.*;

/**
 * An actor that can move.
 */

public abstract class MovingActor extends Actor {

    private final ArrayList<Location> recentlyVisitedList = new ArrayList<>();
    private static final int MAX_VISITED_LIST_LEN = 10;
    private final Random randomiser;

    /**
     * Creates a moving actor based on one or more sprite images.
     * @param isRotatable: if true, the actor's image may be rotated when the direction changes
     * @param nbSprites: the number of sprite images for the same actor
     * @param seed: the seed for random behaviors of the actor
     */
    public MovingActor(boolean isRotatable, int nbSprites, int seed, CharacterType type) {
        super(isRotatable, type.getFilePath(), nbSprites);
        this.randomiser = new Random(seed);
    }

    /* Some common movement logics in all moving actors */

    /**
     * Checks if the move is valid.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public synchronized boolean isMoveValid() {
        return isValidLocation(getFirstCell());
    }

    /**
     * Gets the first cell in the set direction.
     * @return the first cell in that direction.
     */
    protected Location getFirstCell() {
        return getLocation().getNeighbourLocation(getDirection());
    }

    /**
     * Gets the first cell in the given direction.
     * @param dir: the direction from the current location
     * @return the first cell in that direction.
     */
    protected Location getFirstCell(Location.CompassDirection dir) {
        return getLocation().getNeighbourLocation(dir);
    }

    /**
     * Checks if the location is valid (i.e. in grid and not a wall).
     * @param loc: location to be checked
     * @return true if it's valid, false otherwise.
     */
    protected boolean isValidLocation(Location loc) {
        return isInBound(loc) && !isWallAt(loc);
    }

    /* for simplicity of reference */
    /**
     * Checks whether the given location is not a wall.
     * @param loc: location to be checked
     * @return true if it's a wall, false otherwise.
     */
    protected boolean isWallAt(Location loc) {
        return ((Level) gameGrid).getSettingManager().isWallAt(loc);
    }

    /**
     * Checks if the location is in bound of the grid.
     * @param loc: location to be checked
     * @return true if it's in bound, false otherwise.
     */
    protected boolean isInBound(Location loc) {
        return ((Level) gameGrid).getSettingManager().isInBound(loc);
    }

    /**
     * Sets the direction (compass direction restricted to 4 sectors)
     * to the target location.
     * @param target: the target location
     */
    protected void setDirectionToTarget(Location target) {
        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(target);
        setDirection(compassDir);
    }


    /**
     * Sets a direction for the actor.
     * Should be overridden to implement the specific walking approach.
     */
    protected abstract void setNextDirection();

    /**
     * Called in every simulation iteration.
     */
    @Override
    public void act() {
        this.setNextDirection(); // ensures valid direction
        this.move();
        super.act();
    }

    @Override
    public synchronized void move() {
        this.setLocation(getNextMoveLocation());
    }

    /**
     * Gets the target location of the next move().
     * If the next move is the portal, the returned value will be the partner of the portal.
     * If the move is valid, the location is determined by the set direction.
     * If not, the location is the current location (i.e. actor does not move).
     * @return the target location.
     */
    @Override
    public synchronized Location getNextMoveLocation() {
        Item item = ((Level) this.gameGrid).getSettingManager().getItem(getFirstCell());
        if (item instanceof Portal) {
            return ((Portal) item).getPartnerLocation();
        }

        if (isMoveValid()) // in case no directions are valid
            return super.getNextMoveLocation();
        return getLocation();
    }

    /**
     * Sets the direction according to a random walk approach
     * @param oldDirection: original direction
     */
    protected void setRandomMoveDirection(double oldDirection) {
        final int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;

        setDirection(oldDirection);
        // randomly turn left or right
        turn(sign * 90);
        if (isMoveValid())  return;

        // turn back to original direction
        setDirection(oldDirection);
        if (isMoveValid())  return;

        // turn the other side
        turn(-sign * 90);
        if (isMoveValid())  return;

        // go backward
        setDirection(oldDirection);
        turn(180); // turn again to backward.
    }

    /**
     * Adds the given location to the recently visited list.
     * @param location: location to be added
     */
    protected void addVisitedList(Location location) {
        recentlyVisitedList.add(location);
        if (recentlyVisitedList.size() == MAX_VISITED_LIST_LEN)
            recentlyVisitedList.remove(0);
    }

    /**
     * Checks whether the given location is (recently) visited.
     * @param location: the location to be checked
     * @return true if it's (recently) visited, false otherwise.
     */
    protected boolean isVisited(Location location) {
        return recentlyVisitedList.contains(location);
    }

    /**
     * Gets a random integer in [0, range).
     * @param range: upper bound of the range
     * @return the random integer.
     */
    protected int getRandomNumber(int range){
        return randomiser.nextInt(range);
    }
}
