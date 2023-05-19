package game.Items;

import ch.aplu.jgamegrid.Location;

/**
 * The closure to determine if a location satisfies a given predicate.
 */
public interface LocationPredicate {

    /**
     * The predicate to determine if the location satisfies a certain condition.
     *
     * @param location The target location, on which the condition is determined.
     * @return Whether the condition is satisfied on the given location.
     */
    boolean satisfies(Location location);

}
