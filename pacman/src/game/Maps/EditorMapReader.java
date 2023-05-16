package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

public class EditorMapReader implements MapReader {
    HashMap<Location, ActorType> characterLocations;
    HashMap<Location, ActorType> itemLocations;

    ArrayList<Location> wallLocations;

    public EditorMapReader(EditorMap map) {
        characterLocations = new HashMap<>();
        itemLocations = new HashMap<>();
        wallLocations = new ArrayList<>();

        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                ActorType type = map.getTypeAt(new Location(j,i));
                if (type instanceof CellType) {
                    itemLocations.put(new Location(j, i), type);
                    if (((CellType) type) == CellType.WALL) {
                        wallLocations.add(new Location(j, i));
                    }
                } else {
                    characterLocations.put(new Location(j, i), type);
                    itemLocations.put(new Location(j, i), CellType.SPACE);
                }

            }
        }
    }

    @Override
    public HashMap<Location, ActorType> getCharacterLocations() {
        return this.characterLocations;
    }

    @Override
    public HashMap<Location, ActorType> getItemLocations() {
        return this.itemLocations;
    }

    @Override
    public boolean isWallAt(Location location) {
        return wallLocations.contains(location);
    }
}
