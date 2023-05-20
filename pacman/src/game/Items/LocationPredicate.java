package game.Items;

import ch.aplu.jgamegrid.Location;
import game.LocationExpert;

/**
 * The closure to determine if a location satisfies a given predicate.
 */
public interface LocationPredicate {

    /**
     * The predicate to determine if the location satisfies a certain condition.
     *
     * @param location The target location, on which the condition is determined.
     * @param expert The information expert passed to findPath(_:,_:,_:)
     *
     * @return Whether the condition is satisfied on the given location.
     */
    boolean satisfies(Location location, LocationExpert expert);

}
