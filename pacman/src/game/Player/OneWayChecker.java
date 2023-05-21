package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.LocationExpert;
import game.Maps.PacManMap;

import java.util.HashMap;

/**
 * The checker to determine if a path is one way, and could be cornered by monsters.
 */
public class OneWayChecker {

    private final LocationExpert locationExpert;


    public OneWayChecker(LocationExpert locationExpert) {
        this.locationExpert = locationExpert;
    }

    /// 0 for false, 1 for true, 2+ for multiple.
    /**
     * Determines whether it is one way walk given the direction and location.
     *
     * @param location The current location of the pacPlayer.
     * @param direction The direction of the pacPlayer. Only the direction the pacPlayer is facing is considered.
     *
     * @return Whether it is one way.
     */
    public int isOneWayAt(Location location, int direction) {
        if (!locationExpert.isInBound(location.getNeighbourLocation(direction))
                || locationExpert.isWallAt(location.getNeighbourLocation(direction))) {
            return 0;
        }

        // assuming the direction is up
        boolean leftIsWall  = !locationExpert.isInBound(location.getNeighbourLocation(direction).getNeighbourLocation(direction + 90)) || locationExpert.isWallAt(location.getNeighbourLocation(direction).getNeighbourLocation(direction + 90));
        boolean rightIsWall =
                !locationExpert.isInBound(location.getNeighbourLocation(direction).getNeighbourLocation(direction - 90))
                        || locationExpert.isWallAt(location.getNeighbourLocation(direction).getNeighbourLocation(direction - 90));

        if (leftIsWall && rightIsWall) {
            return isOneWayAt(location.getNeighbourLocation(direction), direction);
        } else if (leftIsWall) {
            // left is wall, but fwd and right is not.
            return isOneWayAt(location.getNeighbourLocation(direction), direction) + isOneWayAt(location.getNeighbourLocation(direction), direction + 90);
        } else if (rightIsWall) {
            // left is wall, but fwd and right is not.
            return isOneWayAt(location.getNeighbourLocation(direction), direction) + isOneWayAt(location.getNeighbourLocation(direction), direction - 90);
        } else {
            return 2; // considered okay to walk here.
        }
    }

}
