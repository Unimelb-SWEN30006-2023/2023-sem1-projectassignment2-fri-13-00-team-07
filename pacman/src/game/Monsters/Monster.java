package game.Monsters;

import ch.aplu.jgamegrid.Location;
import game.CharacterType;
import game.Level;
import game.MovingActor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Abstract class for a monster.
 */

public abstract class Monster extends MovingActor {
    /**
     * Creates a monster with one sprite image.
     * @param seed: the seed for random behaviors of the monster
     */
    public Monster(int seed, CharacterType type) {
        super(false, 1, seed, type);
    }

    /**
     * Moves to an appropriate cell,
     * depending on the current state and direction.
     */
    @Override
    public synchronized void move() {
        setHorzMirror(!(getDirection() > 150) || !(getDirection() < 210));
        addVisitedList(getNextMoveLocation());
        super.move(); // actual move

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
     * Gets all 8 neighbor locations, sorted based on the comparator.
     * @param comparator: Comparator on which the sorting is based
     * @return an ArrayList of the sorted neighbor locations.
     */
    protected ArrayList<Location> getSortedNeighborLocations(Comparator<Location> comparator) {
        ArrayList<Location> neighborLocations = getNeighborLocations();
        neighborLocations.sort(comparator);
        return neighborLocations;
    }

    /**
     * Gets all 8 'neighbor' (state-dependent) locations around the monster.
     * @return an ArrayList containing the 8 neighboring locations.
     */
    private ArrayList<Location> getNeighborLocations() {
        ArrayList<Location> neighborLocations = new ArrayList<>();
        for (Location.CompassDirection dir : Location.CompassDirection.values()) {
            neighborLocations.add(getFirstCell(dir));
        }
        return neighborLocations;
    }
}