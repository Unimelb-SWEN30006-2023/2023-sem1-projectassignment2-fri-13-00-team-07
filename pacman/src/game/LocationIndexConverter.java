package game;

import ch.aplu.jgamegrid.Location;

public class LocationIndexConverter {
    private int horizontalCellsCount;
    private static LocationIndexConverter instance = null;

    private LocationIndexConverter(int horizontalCellsCount) {
        this.horizontalCellsCount = horizontalCellsCount;
    }

    public static LocationIndexConverter getInstance(int horizontalCellsCount) {
        if (instance == null)
            instance = new LocationIndexConverter(horizontalCellsCount);
        else if (instance.horizontalCellsCount != horizontalCellsCount)
            instance.horizontalCellsCount = horizontalCellsCount;
        return instance;
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
