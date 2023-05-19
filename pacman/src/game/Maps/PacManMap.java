package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;

/**
 * A map representation.
 */
public interface PacManMap {

    /**
     * Returns the type of cell at the given location.
     *
     * @return The type of given cell.
     */
    ActorType getTypeAt(Location location);

    /**
     * @return The width of the map.
     */
    int getHorizontalCellsCount();

    /**
     * @return The height of the map.
     */
    int getVerticalCellsCount();

    /**
     * @param location The target location.
     *
     * @return Whether there is a wall at the target location.
     */
    boolean isWallAt(Location location);

    /**
     * @param location The target location.
     *
     * @return Whether the location is within the rectangle of map.
     */
    default boolean isInBound(Location location) {
        return location.x >= 0 && location.x < getHorizontalCellsCount()
                && location.y >= 0 && location.y < getVerticalCellsCount();
    }
}
