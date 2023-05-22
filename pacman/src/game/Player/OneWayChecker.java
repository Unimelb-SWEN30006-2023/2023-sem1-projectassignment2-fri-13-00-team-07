package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.LocationExpert;
import game.Maps.PacManMap;

import java.util.HashMap;

/**
 * Checker for determining if a path is one way, and could be cornered by monsters.
 */
public class OneWayChecker {

    private final LocationExpert locationExpert;

    /**
     * Constructs a OneWayChecker, using information from the location expert.
     * @param locationExpert: information expert of the locations in the game.
     */
    public OneWayChecker(LocationExpert locationExpert) {
        this.locationExpert = locationExpert;
    }

    /**
     * Determines whether it is one way walk given the direction and location.
     * Uses recursion.
     *
     * @param location: The current location of the pacPlayer.
     * @param direction: The direction of the pacPlayer.
     *                   Only the direction the pacPlayer is facing is considered.
     *
     * @return an integer representing whether it is a one way walk.
     *         0 for false, 1 for true, 2+ for multiple.
     */
    public int isOneWayAt(Location location, int direction) {
        if (!locationExpert.isInBound(location.getNeighbourLocation(direction))
                || locationExpert.isWallAt(location.getNeighbourLocation(direction))) {
            return 0;
        }

        // assuming the direction is up
        boolean leftIsWall  = !locationExpert.isInBound(getLeftLocation(location, direction))
                              || locationExpert.isWallAt(getLeftLocation(location, direction));
        boolean rightIsWall = !locationExpert.isInBound(getRightLocation(location, direction))
                              || locationExpert.isWallAt(getRightLocation(location, direction));

        if (leftIsWall && rightIsWall) {
            // both left and right is wall, check forward
            return isOneWayAt(location.getNeighbourLocation(direction), direction);
        } else if (leftIsWall) {
            // left is wall, but forward and right is not.
            return isOneWayAt(location.getNeighbourLocation(direction), direction)
                    + isOneWayAt(location.getNeighbourLocation(direction), direction + 90);
        } else if (rightIsWall) {
            // right is wall, but forward and left is not.
            return isOneWayAt(location.getNeighbourLocation(direction), direction)
                    + isOneWayAt(location.getNeighbourLocation(direction), direction - 90);
        } else {
            // okay to walk here.
            return 2;
        }
    }

    /**
     * Gets the left location relative to the current direction faced.
     * @param loc: current location
     * @param direction: current direction faced.
     * @return the left location.
     */
    private Location getLeftLocation(Location loc, int direction) {
        return loc.getNeighbourLocation(direction).getNeighbourLocation(direction + 90);
    }

    /**
     * Gets the right location relative to the current direction faced.
     * @param loc: current location
     * @param direction: current direction faced.
     * @return the right location.
     */
    private Location getRightLocation(Location loc, int direction) {
        return loc.getNeighbourLocation(direction).getNeighbourLocation(direction - 90);
    }
}
