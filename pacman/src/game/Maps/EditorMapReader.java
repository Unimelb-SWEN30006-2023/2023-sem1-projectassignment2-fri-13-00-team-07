package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;

import java.util.HashMap;

public class EditorMapReader implements MapReader {
    HashMap<Location, ActorType> characterLocations;
    HashMap<Location, ActorType> itemLocations;

    public EditorMapReader(EditorMap map) {
        characterLocations = new HashMap<>();
        itemLocations = new HashMap<>();

        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                ActorType type = map.getTypeAt(new Location(j,i));
                if (type instanceof CellType) {
                    itemLocations.put(new Location(j, i), type);
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
}
