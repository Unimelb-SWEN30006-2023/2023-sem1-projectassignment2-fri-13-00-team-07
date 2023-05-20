package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.LocationPredicate;
import game.Maps.PacManMap;
import game.Monsters.Monster;
import game.SettingManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Strategy for finding a path, used by the AutoPlayer.
 */

public interface PathFindingStrategy {
    LinkedList<Location> findPath(Location source, LocationPredicate predicate,
                                  PacManMap map, HashMap<Integer, ActorType> itemLocations,
                                  ArrayList<Monster> monsters);

}
