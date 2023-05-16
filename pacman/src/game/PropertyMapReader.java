package game;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

public class PropertyMapReader implements MapReader {
    private PacManGameGrid grid = new PacManGameGrid();
    private PropertyReader propertyReader;

    public PropertyMapReader(PacManGameGrid map, PropertyReader propertyReader) {
        this.grid = map;
        this.propertyReader = propertyReader;
    }

    @Override
    public HashMap<Location, ActorType> getCharacterLocations() {
        HashMap<Location, ActorType> characterLocations = new HashMap<Location, ActorType>();
        CharacterType characters[] = new CharacterType[]{CharacterType.PACMAN, CharacterType.M_TROLL, CharacterType.M_TX5};
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
                ActorType cellType = grid.getTypeAt(location);
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

    @Override
    public PacManMap getMap() {
        return grid;
    }
}
