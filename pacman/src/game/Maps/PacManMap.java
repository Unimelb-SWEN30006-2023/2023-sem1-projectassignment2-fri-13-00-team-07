package game.Maps;

import game.ActorType;
import game.LocationExpert;
import game.Workers.MapReader;

import java.util.HashMap;

/**
 * A static location expert, i.e. a map representation in the PacMan game.
 */
public interface PacManMap extends LocationExpert {
    /* Applying the visitor pattern here */
    /**
     * Reads the map using the given map reader, to attain the item locations.
     * @param reader: MapReader for reading the map.
     * @return a HashMap containing the ActorType (should contain only CellTypes)
     *         for each location (represented by an integer index).
     */
    HashMap<Integer, ActorType> readMyItemLocations(MapReader reader);

    /**
     * Reads the map using the given map reader, to attain the characters' locations.
     * @param reader: MapReader for reading the map.
     * @return a HashMap containing the ActorType (should contain only CharacterTypes)
     *         for each location (represented by an integer index).
     */
    HashMap<Integer, ActorType> readMyCharacterLocations(MapReader reader);
}
