package game;

import ch.aplu.jgamegrid.Location;

public class PacManMap extends PacManGameGrid {
    private ActorType[][] map;

    public PacManMap(ActorType[][] map) {
        this.map = map;
    }

    @Override
    public CellType getCellType(Location loc) {
        return isCellType(loc) ? ((CellType) map[loc.x][loc.y]) : null;
    }

    public boolean isCellType(Location loc) {
        return map[loc.x][loc.y] instanceof CellType;
    }
}
