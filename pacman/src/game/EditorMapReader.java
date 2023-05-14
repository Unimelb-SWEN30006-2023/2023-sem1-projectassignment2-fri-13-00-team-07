package game;

import ch.aplu.jgamegrid.Location;

import java.util.HashMap;

public class EditorMapReader implements MapReader {
    private ActorType[][] map;

    @Override
    public HashMap<Location, ActorType> getCharacterLocations() {
        HashMap<Location, ActorType> characterLocations = new HashMap<>();
        int numRows = map.length;
        int numCols = map[0].length;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (map[i][j] instanceof CharacterType)
                    characterLocations.put(new Location(i, j), map[i][j]);
            }
        }
        return characterLocations;
    }

    @Override
    public HashMap<Location, ActorType> getItemLocations() {
        HashMap<Location, ActorType> itemLocations = new HashMap<Location, ActorType>();
        int numRows = map.length;
        int numCols = map[0].length;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (map[i][j] instanceof CellType)
                    itemLocations.put(new Location(i, j), map[i][j]);
            }
        }
        return itemLocations;
    }
}
