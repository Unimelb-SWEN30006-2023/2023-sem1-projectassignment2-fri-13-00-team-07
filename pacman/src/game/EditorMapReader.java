package game;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;

import java.util.HashMap;

public class EditorMapReader implements MapReader {
    private EditorMap map;

    public EditorMapReader(EditorMap map) {
        this.map = map;
    }

    @Override
    public HashMap<Location, ActorType> getCharacterLocations() {
        HashMap<Location, ActorType> characterLocations = new HashMap<>();

        for (int i = 0; i < map.getNumRows(); i++) {
            for (int j = 0; j < map.getNumCols(); j++) {
                ActorType type = map.getTypeAt(new Location(i,j));
                if (type instanceof CharacterType)
                    characterLocations.put(new Location(i, j), type);
            }
        }
        return characterLocations;
    }

    @Override
    public HashMap<Location, ActorType> getItemLocations() {
        HashMap<Location, ActorType> itemLocations = new HashMap<Location, ActorType>();
        for (int i = 0; i < map.getNumRows(); i++) {
            for (int j = 0; j < map.getNumCols(); j++) {
                ActorType type = map.getTypeAt(new Location(i,j));
                if (type instanceof CellType)
                    itemLocations.put(new Location(i, j), type);
            }
        }
        return itemLocations;
    }

    @Override
    public PacManMap getMap() {
        return map;
    }
}
