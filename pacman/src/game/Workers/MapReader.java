package game.Workers;

import game.ActorType;
import game.Maps.PacManMap;

import java.util.HashMap;

/**
 * Map reader for attaining the item and character locations information from
 * a PacManMap.
 */
public interface MapReader {
    /**
     * Reads the map to get the item locations.
     * @param map: the map to be read
     * @return a HashMap mapping the location (represented by an integer index)
     *         to each type of item.
     */
    HashMap<Integer, ActorType> getItemLocations(PacManMap map);

    /**
     * Reads the map to get the character locations.
     * @param map: the map to be read
     * @return a HashMap mapping the location (represented by an integer index)
     *         to each type of character.
     */
    HashMap<Integer, ActorType> getCharacterLocations(PacManMap map);
}
