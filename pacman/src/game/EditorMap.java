package game;

import ch.aplu.jgamegrid.Location;


public class EditorMap implements PacManMap {
    private ActorType[][] map;
    private String name; //Ziming: I want a name here

    public EditorMap(ActorType[][] map) {
        this.map = map;
    }

    public String getName(){
        return name;
    }

    @Override
    public ActorType getTypeAt(Location loc) {
        return map[loc.x][loc.y];
    }

    public boolean isCellType(Location loc) {
        return map[loc.x][loc.y] instanceof CellType;
    }

    public boolean isCharacterType(Location loc){
        return map[loc.x][loc.y] instanceof CharacterType;
    }

    public int getNumRows() {
        return map.length;
    }

    public int getNumCols() {
        return map[0].length;
    }
}
