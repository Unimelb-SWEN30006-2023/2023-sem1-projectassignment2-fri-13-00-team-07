package game;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

public interface LocationExpert {
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

    default HashMap<CellType, ArrayList<Location>> getPortalLocations() {
        HashMap<CellType, ArrayList<Location>> portalLocations = new HashMap<>();
        for (int y = 0; y < getVerticalCellsCount(); y++) {
            for (int x = 0; x < getHorizontalCellsCount(); x++) {
                Location location = new Location(x, y);
                ActorType cellType = getTypeAt(location);

                if (cellType instanceof CellType && ((CellType) cellType).isPortal()) {
                    portalLocations.computeIfAbsent((CellType) cellType, k -> new ArrayList<>());
                    portalLocations.get(cellType).add(location);
                }
            }
        }
        return portalLocations;
    }
}
