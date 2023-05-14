package game;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class PropertyMapReader {
    private PacManGameGrid grid;
    private PropertyReader propertyReader;

    public PropertyMapReader(Properties properties, PacManGameGrid grid) {
        this.grid = grid;
        this.propertyReader = ServicesFactory.getInstance().getPropertyReader(properties);
    }

    @Override
    public HashMap<Location, ActorType> getCharacterLocations() {
        HashMap<Location, ActorType> characterLocations = new HashMap<Location, ActorType>();
        CharacterType characters[] = new CharacterType[]{CharacterType.PACMAN, CharacterType.TROLL_M, CharacterType.TX5_M};
        for (CharacterType character : characters) {
            Location location = propertyReader.readLocation(character.getName() + ".location");
            if (location != null && !(location.equals(new Location(-1, -1))))
                characterLocations.put(location, character);
        }
        return characterLocations;
    }

    @Override
    public HashMap<Location, ActorType> getItemLocations() {
        HashMap<Location, ActorType> itemLocations = new HashMap<Location, ActorType>();
        ArrayList<Location> propertyPillLocations = propertyReader.loadLocations("Pills.location");
        ArrayList<Location> propertyGoldLocations = propertyReader.loadLocations("Gold.location");

        boolean useMazePillLocations = (propertyPillLocations.size() == 0);
        boolean useMazeGoldLocations = (propertyGoldLocations.size() == 0);

        for (int y = 0; y < Level.getNumVertCells(); y++) {
            for (int x = 0; x < Level.getNumHorzCells(); x++) {
                Location location = new Location(x, y);
                CellType cellType = grid.getCellType(location);
                if (cellType.equals(CellType.GOLD)) {
                    if (useMazeGoldLocations)
                        itemLocations.put(location, cellType);
                } else if (cellType.equals(CellType.PILL)) {
                    if (useMazePillLocations)
                        itemLocations.put(location, cellType);
                } else {
                    itemLocations.put(location, cellType);
                }
            }
        }

        // put properties pills (does nothing if these lists are empty)
        for (Location location : propertyGoldLocations)
            itemLocations.put(location, CellType.GOLD);
        for (Location location : propertyPillLocations)
            itemLocations.put(location, CellType.PILL);

        return itemLocations;
    }

}
