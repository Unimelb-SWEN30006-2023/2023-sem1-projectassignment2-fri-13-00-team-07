package game.Player;

import ch.aplu.jgamegrid.Location;
import game.Items.LocationPredicate;
import game.LocationExpert;
import game.Monsters.Monster;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Strategy for finding a path, used by the AutoPacPlayer.
 */

public interface PathFindingStrategy {

    /**
     * Finds the optimal path to a sink that satisfies the `predicate` and avoids the monsters.
     *
     * @param source The source location.
     * @param predicate The predicate for a location to be considered a destination.
     * @param locationExpert The expert that stores the locations to items on map
     * @param monsters The monsters on the map. The monsters are avoided when finding the path.
     *
     * @return The optimal path, null on failure or empty.
     */
    LinkedList<Location> findPath(Location source,
                                  LocationPredicate predicate,
                                  LocationExpert locationExpert,
                                  ArrayList<Monster> monsters);

    /**
     * Finds the optimal path to a sink that satisfies the `predicate`.
     *
     * @param source The source location.
     * @param predicate The predicate for a location to be considered a destination.
     * @param locationExpert The expert that stores the locations to items on map
     *
     * @return The optimal path, null on failure or empty.
     */
    default LinkedList<Location> findPath(Location source,
                                                         LocationPredicate predicate,
                                                         LocationExpert locationExpert
    ) {
        return findPath(source, predicate, locationExpert, null);
    }

    /**
     * Finds the optimal path to the sink.
     *
     * @param source The source location.
     * @param sink The destination.
     * @param locationExpert The expert that stores the locations to items on map
     *
     * @return The optimal path, null on failure or empty.
     */
    default LinkedList<Location> findPath(Location source,
                                          Location sink,
                                          LocationExpert locationExpert
    ) {
        return findPath(source, (i, expert) -> i.equals(sink), locationExpert, null);
    }



}
