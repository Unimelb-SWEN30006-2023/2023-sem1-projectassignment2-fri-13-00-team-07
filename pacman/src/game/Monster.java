package game;

import ch.aplu.jgamegrid.Location;

import java.util.*;

/**
 * Abstract class for a monster.
 */

public abstract class Monster extends MovingActor {

    private MonsterState state = MonsterState.NORMAL;
    private Timer stateTimer = new Timer();

    /**
     * Creates a monster with one sprite image.
     * @param seed: the seed for random behaviors of the monster
     */
    public Monster(int seed, CharacterType type) {
        super(seed, type);
    }

    /**
     * Moves to an appropriate cell,
     * depending on the current state and direction.
     */
    @Override
    public synchronized void move() {
        if (isFrozen())
            return;

        setHorzMirror(!(getDirection() > 150) || !(getDirection() < 210));
        addVisitedList(getNextMoveLocation());
        setLocation(getNextMoveLocation()); // actual move

        ((Level) gameGrid).getGameCallback().monsterLocationChanged(this);
    }

    /**
     * Gets the monster type (by its class name).
     * @return the monster type as a string.
     */
    public String getType() {
        return getClass().getSimpleName();
    }


    /**
     * Sets the state of the current monster.
     */
    public void setState(MonsterState state) {
        switch (state) {
            case NORMAL:
                break;
            case FROZEN:
            case FURIOUS:
                if (isFrozen()) return;
            default:
                refreshStateTimer();
        }

        this.state = state;
    }

    /**
     * Refreshes the state timer.
     */
    private void refreshStateTimer() {
        stateTimer.cancel(); // cancel all other states
        /* furious -> furious: reset timer, cancel
         * furious -> frozen: reset timer, cancel
         * frozen -> furious: can't do
         * frozen -> frozen: reset timer, cancel
         */

        int SECOND_TO_MILLISECONDS = 1000;
        Monster monster = this;
        stateTimer = new Timer();
        stateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.state = MonsterState.NORMAL;
            }
        }, (long) 3 * SECOND_TO_MILLISECONDS);
    }

    /**
     * Gets the location of the next move, depending on the state.
     * @return the next move's location.
     */
    @Override
    public synchronized Location getNextMoveLocation() {
        if (!isFurious())
            return super.getNextMoveLocation(); // move one cell

        // furious state
        if (isMoveValid())
            return getSecondCell(); // move two cells
        return getLocation(); // cannot move two cells -> do not move
    }

    /**
     * Gets the second cell in the set direction.
     * @return the second cell in that direction.
     */
    protected Location getSecondCell() {
        return getFirstCell().getNeighbourLocation(getDirection());
    }

    /**
     * Gets the second cell in the given direction.
     * @param dir: the direction from the current location
     * @return the second cell in that direction.
     */
    protected Location getSecondCell(Location.CompassDirection dir) {
        return getFirstCell(dir).getNeighbourLocation(dir);
    }

    /**
     * Checks if the next move is valid.
     * In furious state, checks both the first and second cell.
     */
    @Override
    public synchronized boolean isMoveValid() {
        if (!isFurious()) // check first cell only
            return super.isMoveValid();

        // check both cells
        return isValidLocation(getFirstCell()) && isValidLocation(getSecondCell());
    }

    /* Common logic for some monsters */

    /**
     * Gets all 8 neighbor locations, sorted based on the comparator.
     * @param comparator: Comparator on which the sorting is based
     * @return an ArrayList of the sorted neighbor locations.
     */
    protected ArrayList<Location> getSortedNeighborLocations(Comparator<Location> comparator) {
        ArrayList<Location> neighborLocations = getNeighborLocations();
        Collections.sort(neighborLocations, comparator);
        return neighborLocations;
    }

    /**
     * Gets all 8 'neighbor' (state-dependent) locations around the monster.
     * @return an ArrayList containing the 8 neighboring locations.
     */
    private ArrayList<Location> getNeighborLocations() {
        ArrayList<Location> neighborLocations = new ArrayList<>();
        for (Location.CompassDirection dir : Location.CompassDirection.values()) {
            if (isFurious()) // furious state -> 'neighbor' = second cell
                neighborLocations.add(getSecondCell(dir));
            else
                neighborLocations.add(getFirstCell(dir));
        }
        return neighborLocations;
    }

    /**
     * Checks if the monster is furious.
     * @return true if it is furious, false otherwise.
     */
    protected boolean isFurious() {
        return state == MonsterState.FURIOUS;
    }

    /**
     * Checks if the monster is frozen.
     * @return true if it is frozen, false otherwise.
     */
    protected boolean isFrozen() {
        return state == MonsterState.FROZEN;
    }
}