package game;

import ch.aplu.jgamegrid.Location;


public class EditorMap implements PacManMap {
    private ActorType[][] map;

    public EditorMap(ActorType[][] map) {
        this.map = map;
    }

    @Override
    public ActorType getTypeAt(Location loc) {
        return map[loc.x][loc.y];
    }

    public boolean isCellType(Location loc) {
        return map[loc.x][loc.y] instanceof CellType;
    }

    public int getNumRows() {
        return map.length;
    }

    public int getNumCols() {
        return map[0].length;
    }
}
