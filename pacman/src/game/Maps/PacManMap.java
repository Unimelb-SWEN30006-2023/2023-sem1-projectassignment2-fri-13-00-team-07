package game.Maps;

import game.ActorType;
import game.LocationExpert;
import game.Workers.MapReader;

import java.util.HashMap;

/**
 * A map representation.
 */
public interface PacManMap extends LocationExpert {
    HashMap<Integer, ActorType> readMyItemLocations(MapReader reader);
    HashMap<Integer, ActorType> readMyCharacterLocations(MapReader reader);
}
