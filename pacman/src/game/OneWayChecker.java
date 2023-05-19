package game;

import ch.aplu.jgamegrid.Location;
import game.Maps.PacManMap;

/**
 * The checker to determine if a path is one way, and could be cornered by monsters.
 */
public class OneWayChecker {

    private static OneWayChecker instance;

    /**
     * @return the only instance.
     */
    public static OneWayChecker getInstance() {
        if (instance == null) { instance = new OneWayChecker(); }
        return instance;
    }

    /// 0 for false, 1 for true, 2+ for multiple.

    /**
     * Determines whether it is one way walk given the direction and location.
     *
     * @param location The current location of the player.
     * @param map The map the player presents.
     * @param direction The direction of the player. Only the direction the player is facing is considered.
     * @return
     */
    public int isOneWayAt(Location location, PacManMap map, int direction) {
        if (!map.isInBound(location.getNeighbourLocation(direction)) || map.isWallAt(location.getNeighbourLocation(direction))) {
            return 0;
        }

        // assuming the direction is up
        boolean leftIsWall  = !map.isInBound(location.getNeighbourLocation(direction).getNeighbourLocation(direction + 90)) || map.isWallAt(location.getNeighbourLocation(direction).getNeighbourLocation(direction + 90));
        boolean rightIsWall =
                !map.isInBound(location.getNeighbourLocation(direction).getNeighbourLocation(direction - 90)) || map.isWallAt(location.getNeighbourLocation(direction).getNeighbourLocation(direction - 90));

        if (leftIsWall && rightIsWall) {
            return isOneWayAt(location.getNeighbourLocation(direction), map, direction);
        } else if (leftIsWall) {
            // left is wall, but fwd and right is not.
            return isOneWayAt(location.getNeighbourLocation(direction), map, direction) + isOneWayAt(location.getNeighbourLocation(direction), map, direction + 90);
        } else if (rightIsWall) {
            // left is wall, but fwd and right is not.
            return isOneWayAt(location.getNeighbourLocation(direction), map, direction) + isOneWayAt(location.getNeighbourLocation(direction), map, direction - 90);
        } else {
            return 2; // considered okay to walk here.
        }
    }

}
