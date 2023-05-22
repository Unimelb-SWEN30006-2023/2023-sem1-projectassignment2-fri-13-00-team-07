package game.Workers;

import ch.aplu.jgamegrid.Location;
import game.*;
import game.Items.CellType;
import game.Maps.PacManMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Reader for the PacManGameGrid which also uses information from the properties
 * file. (i.e. Preserves the original game's behavior.)
 */
public class PropertyMapReader implements MapReader {
    private final HashMap<Integer, ActorType> characterLocations = new HashMap<>();
    private final HashMap<Integer, ActorType> itemLocations = new HashMap<>();
    /* Need info from the properties file -> Ask the propertyReader for help */
    private PropertyReader propertyReader;

    /**
     * Constructs a PropertyMapReader which employs the given property reader.
     * @param propertyReader: A PropertyReader used to help the PropertyMapReader
     *                        to attain information from the properties file.
     */
    public PropertyMapReader(PropertyReader propertyReader) {
        // By default, use the standard PacManGameGrid
        this.propertyReader = propertyReader;
    }

    /**
     * Reads the 'property' map (properties file + PacManGameGrid)
     * and stores the actors' location information.
     * @param map: the map to be read (should be a PacManGameGrid).
     */
    private void readPropertyMap(PacManMap map) {
        LocationIndexConverter indexConverter = new LocationIndexConverter(map.getHorizontalCellsCount());

        for (CharacterType character : CharacterType.CHARACTER_TYPES) {
            Location location = propertyReader.readLocation(character.getName() + ".location");
            if (location != null && !(location.equals(new Location(-1, -1))))
                characterLocations.put(indexConverter.getIndexByLocation(location), character);
        }

        /* Get information from the properties file */
        ArrayList<Location> propertyPillLocations = propertyReader.loadLocations("Pills.location");
        ArrayList<Location> propertyGoldLocations = propertyReader.loadLocations("Gold.location");

        boolean useMazePillLocations = (propertyPillLocations.size() == 0);
        boolean useMazeGoldLocations = (propertyGoldLocations.size() == 0);

        for (int y = 0; y < Level.DEFAULT_NB_VERT_CELLS; y++) {
            for (int x = 0; x < Level.DEFAULT_NB_HORZ_CELLS; x++) {
                Location location = new Location(x, y);
                ActorType cellType = map.getTypeAt(location);
                if (cellType.equals(CellType.GOLD)) {
                    if (useMazeGoldLocations) {
                        itemLocations.put(indexConverter.getIndexByLocation(location), cellType);
                    } else {
                        itemLocations.put(indexConverter.getIndexByLocation(location), CellType.SPACE);
                    }
                } else if (cellType.equals(CellType.PILL)) {
                    if (useMazePillLocations) {
                        itemLocations.put(indexConverter.getIndexByLocation(location), cellType);
                    } else {
                        itemLocations.put(indexConverter.getIndexByLocation(location), CellType.SPACE);
                    }
                } else {
                    itemLocations.put(indexConverter.getIndexByLocation(location), cellType);
                }
            }
        }

        /* put properties pills (does nothing if these lists are empty) */
        for (Location location : propertyGoldLocations)
            itemLocations.put(indexConverter.getIndexByLocation(location), CellType.GOLD);
        for (Location location : propertyPillLocations)
            itemLocations.put(indexConverter.getIndexByLocation(location), CellType.PILL);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, ActorType> getCharacterLocations(PacManMap map) {
        readPropertyMap(map);
        return characterLocations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, ActorType> getItemLocations(PacManMap map) {
        readPropertyMap(map);
        return itemLocations;
    }
}
