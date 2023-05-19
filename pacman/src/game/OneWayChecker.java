package game;

import ch.aplu.jgamegrid.Location;
import game.Maps.PacManMap;

public class OneWayChecker {

    private static OneWayChecker instance;

    public static OneWayChecker getInstance() {
        if (instance == null) { instance = new OneWayChecker(); }
        return instance;
    }

    public int isOneWayAt(Location location, PacManMap map, int direction) {
        return canMoveForward(location, map, direction);
    }

    /// 0 for false, 1 for true, 2+ for multiple.
    private int canMoveForward(Location location, PacManMap map, int direction) {
        if (!map.isInBound(location.getNeighbourLocation(direction)) || map.isWallAt(location.getNeighbourLocation(direction))) {
            return 0;
        }

        // assuming the direction is up
        boolean leftIsWall  = !map.isInBound(location.getNeighbourLocation(direction).getNeighbourLocation(direction + 90)) || map.isWallAt(location.getNeighbourLocation(direction).getNeighbourLocation(direction + 90));
        boolean rightIsWall =
                !map.isInBound(location.getNeighbourLocation(direction).getNeighbourLocation(direction - 90)) || map.isWallAt(location.getNeighbourLocation(direction).getNeighbourLocation(direction - 90));

        if (leftIsWall && rightIsWall) {
            return canMoveForward(location.getNeighbourLocation(direction), map, direction);
        } else if (leftIsWall) {
            // left is wall, but fwd and right is not.
            return canMoveForward(location.getNeighbourLocation(direction), map, direction) + canMoveForward(location.getNeighbourLocation(direction), map, direction + 90);
        } else if (rightIsWall) {
            // left is wall, but fwd and right is not.
            return canMoveForward(location.getNeighbourLocation(direction), map, direction) + canMoveForward(location.getNeighbourLocation(direction), map, direction - 90);
        } else {
            return 2; // to be changed.
        }
    }

}
