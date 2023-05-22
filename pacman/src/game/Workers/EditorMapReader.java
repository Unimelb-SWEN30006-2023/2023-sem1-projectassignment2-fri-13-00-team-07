package game.Workers;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
import game.Maps.PacManMap;

import java.util.HashMap;

/**
 * Reader for a map used for the editor (new functionality as per the spec).
 */
public class EditorMapReader implements MapReader {
    private HashMap<Integer, ActorType> itemLocations = new HashMap<>();
    private HashMap<Integer, ActorType> characterLocations = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, ActorType> getItemLocations(PacManMap map) {
        readMap(map);
        return itemLocations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, ActorType> getCharacterLocations(PacManMap map) {
        readMap(map);
        return characterLocations;
    }

    /**
     * Reads the map and stores the item and character locations.
     * @param map: the map to be read.
     */
    private void readMap(PacManMap map) {
        LocationIndexConverter indexConverter = new LocationIndexConverter(map.getHorizontalCellsCount());
        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                ActorType type = map.getTypeAt(new Location(j,i));
                if (type instanceof CellType) {
                    itemLocations.put(indexConverter.getIndexByLocation(new Location(j, i)), type);
                } else {
                    characterLocations.put(indexConverter.getIndexByLocation(new Location(j, i)), type);
                    // A character tile -> fill it with space
                    itemLocations.put(indexConverter.getIndexByLocation(new Location(j, i)), CellType.SPACE);
                }
            }
        }
    }
}
