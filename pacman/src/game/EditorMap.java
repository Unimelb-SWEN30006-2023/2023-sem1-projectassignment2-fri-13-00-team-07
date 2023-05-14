package game;

import ch.aplu.jgamegrid.Location;


public class EditorMap implements PacManMap {
    private ActorType[][] map;

    public EditorMap(ActorType[][] map) {
        this.map = map;
    }

    @Override
    public ActorType getTypeAt(Location loc) {
        return isCellType(loc) ? map[loc.x][loc.y] : null;
    }

    public boolean isCellType(Location loc) {
        return map[loc.x][loc.y] instanceof CellType;
    }
}
