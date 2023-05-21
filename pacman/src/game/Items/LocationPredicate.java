package game.Items;

import ch.aplu.jgamegrid.Location;
import game.LocationExpert;

/**
 * A functional interface to determine if a location satisfies a certain predicate.
 */
@FunctionalInterface
public interface LocationPredicate {

    /**
     * Determine if the location satisfies a certain condition.
     *
     * @param location: The target location, on which the condition is determined.
     * @param expert: The information expert of locations passed to findPath(_:,_:,_:)
     *
     * @return true if the condition is satisfied on the given location, false otherwise.
     */
    boolean satisfies(Location location, LocationExpert expert);

}
