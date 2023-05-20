package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.LocationExpert;

import java.util.HashMap;

/**
 * A map representation.
 */
public interface PacManMap extends LocationExpert {
    HashMap<Integer, ActorType> readMyItemLocations(MapReader reader);
    HashMap<Integer, ActorType> readMyCharacterLocations(MapReader reader);
}
