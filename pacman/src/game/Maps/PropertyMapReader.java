package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.*;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyMapReader implements MapReader {
    private final PacManGameGrid map;
    private final HashMap<Integer, ActorType> characterLocations;
    private final HashMap<Integer, ActorType> itemLocations;

    public PropertyMapReader(PropertyReader propertyReader) {
        this(new PacManGameGrid(), propertyReader);
    }

    public PropertyMapReader(PacManGameGrid map, PropertyReader propertyReader) {
        this.map = map;

        LocationIndexConverter indexConverter = new LocationIndexConverter(map.getHorizontalCellsCount());

        characterLocations = new HashMap<>();
        CharacterType[] characters = new CharacterType[]{CharacterType.PACMAN, CharacterType.M_TROLL, CharacterType.M_TX5};
        for (CharacterType character : characters) {
            Location location = propertyReader.readLocation(character.getName() + ".location");
            if (location != null && !(location.equals(new Location(-1, -1))))
                characterLocations.put(indexConverter.getIndexByLocation(location), character);
        }


        itemLocations = new HashMap<>();
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

        // put properties pills (does nothing if these lists are empty)
        for (Location location : propertyGoldLocations)
            itemLocations.put(indexConverter.getIndexByLocation(location), CellType.GOLD);
        for (Location location : propertyPillLocations)
            itemLocations.put(indexConverter.getIndexByLocation(location), CellType.PILL);
    }

    @Override
    public PacManMap getMap() {
        return map;
    }

    @Override
    public HashMap<Integer, ActorType> getCharacterLocations() {
        return characterLocations;
    }

    @Override
    public HashMap<Integer, ActorType> getItemLocations() {
        return itemLocations;
    }
}
