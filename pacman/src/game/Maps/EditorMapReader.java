package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
import game.LocationIndexConverter;

import java.util.HashMap;

public class EditorMapReader implements MapReader {
    private HashMap<Integer, ActorType> characterLocations;
    private HashMap<Integer, ActorType> itemLocations;
    private EditorMap map;

    public EditorMapReader(EditorMap map) {
        this.map = map;
        characterLocations = new HashMap<>();
        itemLocations = new HashMap<>();

        LocationIndexConverter indexConverter = LocationIndexConverter.getInstance(map.getHorizontalCellsCount());

        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                ActorType type = map.getTypeAt(new Location(j,i));
                if (type instanceof CellType) {
                    itemLocations.put(indexConverter.getIndexByLocation(new Location(j, i)), type);
                } else {
                    characterLocations.put(indexConverter.getIndexByLocation(new Location(j, i)), type);
                    itemLocations.put(indexConverter.getIndexByLocation(new Location(j, i)), CellType.SPACE);
                }

            }
        }
    }

    @Override
    public HashMap<Integer, ActorType> getCharacterLocations() {
        return this.characterLocations;
    }

    @Override
    public HashMap<Integer, ActorType> getItemLocations() {
        return this.itemLocations;
    }

    @Override
    public PacManMap getMap() {
        return map;
    }
}
