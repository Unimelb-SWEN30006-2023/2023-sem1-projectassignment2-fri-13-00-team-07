package game;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents an information expert for the item locations.
 */
public interface LocationExpert {
    /**
     * Returns the type of cell at the given location.
     * @param: the location to be looked up.
     * @return The type at the given location.
     */
    ActorType getTypeAt(Location location);

    /**
     * Gets the width (number of horizontal cells) in the map.
     * @return The width of the map.
     */
    int getHorizontalCellsCount();

    /**
     * Gets the height (number of vertical cells) in the map.
     * @return The height of the map.
     */
    int getVerticalCellsCount();

    /**
     * Checks if the given location is a wall.
     * @param location: The location to be checked.
     * @return true if there is a wall at the target location, false otherwise.
     */
    boolean isWallAt(Location location);

    /**
     * Checks if the given location is in bound.
     * @param location: The location to be checked.
     * @return true if the location is within the bound of map, false otherwise.
     */
    default boolean isInBound(Location location) {
        return location.x >= 0 && location.x < getHorizontalCellsCount()
                && location.y >= 0 && location.y < getVerticalCellsCount();
    }

    /**
     * Gets the portal locations.
     * @return a HashMap representing the paired portal locations
     *         for each type of portal.
     */
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
