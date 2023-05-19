package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;

public interface PacManMap {
    ActorType getTypeAt(Location location);

    int getHorizontalCellsCount();
    int getVerticalCellsCount();

    boolean isWallAt(Location location);

    default boolean isInBound(Location location) {
        return location.x >= 0 && location.x < getHorizontalCellsCount()
                && location.y >= 0 && location.y < getVerticalCellsCount();
    }
}
