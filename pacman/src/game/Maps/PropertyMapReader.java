package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.*;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyMapReader implements MapReader {
    HashMap<Location, ActorType> characterLocations;
    HashMap<Location, ActorType> itemLocations;

    ArrayList<Location> wallLocations;

    public PropertyMapReader(PacManGameGrid map, PropertyReader propertyReader) {
        characterLocations = new HashMap<Location, ActorType>();
        CharacterType characters[] = new CharacterType[]{CharacterType.PACMAN, CharacterType.M_TROLL, CharacterType.M_TX5};
        for (CharacterType character : characters) {
            Location location = propertyReader.readLocation(character.getName() + ".location");
            if (location != null && !(location.equals(new Location(-1, -1))))
                characterLocations.put(location, character);
        }


        itemLocations = new HashMap<Location, ActorType>();
        ArrayList<Location> propertyPillLocations = propertyReader.loadLocations("Pills.location");
        ArrayList<Location> propertyGoldLocations = propertyReader.loadLocations("Gold.location");

        boolean useMazePillLocations = (propertyPillLocations.size() == 0);
        boolean useMazeGoldLocations = (propertyGoldLocations.size() == 0);

        for (int y = 0; y < Level.DEFAULT_NB_VERT_CELLS; y++) {
            for (int x = 0; x < Level.DEFAULT_NB_HORZ_CELLS; x++) {
                Location location = new Location(x, y);
                ActorType cellType = map.getTypeAt(location);
                if (cellType.equals(CellType.GOLD)) {
                    if (useMazeGoldLocations)
                        itemLocations.put(location, cellType);
                } else if (cellType.equals(CellType.PILL)) {
                    if (useMazePillLocations)
                        itemLocations.put(location, cellType);
                } else {
                    if (cellType == CellType.WALL) {
                        wallLocations.add(location);
                    }
                    itemLocations.put(location, cellType);
                }
            }
        }

        // put properties pills (does nothing if these lists are empty)
        for (Location location : propertyGoldLocations)
            itemLocations.put(location, CellType.GOLD);
        for (Location location : propertyPillLocations)
            itemLocations.put(location, CellType.PILL);
    }

    @Override
    public HashMap<Location, ActorType> getCharacterLocations() {
        return characterLocations;
    }

    @Override
    public HashMap<Location, ActorType> getItemLocations() {
        return itemLocations;
    }

    @Override
    public boolean isWallAt(Location location) {
        return wallLocations.contains(location);
    }
}
