package game.Player;

import ch.aplu.jgamegrid.Location;
import game.Items.LocationPredicate;
import game.Maps.PacManMap;
import game.Monsters.Monster;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Strategy for finding a path, used by the AutoPlayer.
 */

public interface PathFindingStrategy {
    LinkedList<Location> findPath(Location source, LocationPredicate predicate, PacManMap map, ArrayList<Monster> monsters);
}
