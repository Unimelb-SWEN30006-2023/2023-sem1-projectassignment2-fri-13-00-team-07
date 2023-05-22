package game.Workers;

import ch.aplu.jgamegrid.Location;

/**
 * Helper converter for mapping a location to an integer
 * Cannot use location directly as the key, as Location does not implement hashCode correctly.
 * for later lookups
 */
public class LocationIndexConverter {
    private final int horizontalCellsCount;

    public LocationIndexConverter(int horizontalCellsCount) {
        this.horizontalCellsCount = horizontalCellsCount;
    }

    /**
     * Gets the location corresponding to the given index.
     * @param index: an integer specifying the index in the game grid
     * @return the corresponding location.
     */
    public Location getLocationByIndex(int index) {
        return new Location(index % horizontalCellsCount, index / horizontalCellsCount);
    }

    /**
     * Gets the index corresponding to the given location.
     * @param location: location to look up
     * @return the corresponding index in the game grid.
     */
    public int getIndexByLocation(Location location) {
        return location.y * horizontalCellsCount + location.x;
    }
}
