package game;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.util.Properties;

public class SettingManager {
    private MapReader mapReader;
    private PropertyReader propertyReader;
    private ItemManager itemManager;

    public SettingManager(Properties properties, PacManMap map, Level level) {
        propertyReader = new PropertyReader(properties);
        if (map instanceof EditorMap)
            mapReader = new EditorMapReader((EditorMap) map);
        else
            mapReader = new PropertyMapReader((PacManGameGrid) map, propertyReader);

        itemManager = new ItemManager(mapReader.getItemLocations(), level);
    }


    /**
     * Checks whether the given location is not a wall.
     * @param location: location to be checked
     * @return true if it's a wall, false otherwise.
     */
    public boolean isWallAt(Location location) {
        return isInBound(location) && mapReader.getMap().getTypeAt(location) == CellType.WALL;
    }

    /**
     * Checks if the location is in bound of the grid.
     * @param location: location to be checked
     * @return true if it's in bound, false otherwise.
     */
    public boolean isInBound(Location location) {
        return location.x >= 0 && location.x < Level.getNumHorzCells()
                && location.y >= 0 && location.y < Level.getNumVertCells();
    }

    public MapReader getMapReader() {
        return mapReader;
    }

    public PropertyReader getPropertyReader() {
        return propertyReader;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }
}
