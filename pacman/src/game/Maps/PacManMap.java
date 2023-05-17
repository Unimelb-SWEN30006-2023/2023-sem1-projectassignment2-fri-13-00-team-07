package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;

public interface PacManMap {
    ActorType getTypeAt(Location location);

    int getHorizontalCellsCount();
    int getVerticalCellsCount();

    boolean isWallAt(Location location);
}
