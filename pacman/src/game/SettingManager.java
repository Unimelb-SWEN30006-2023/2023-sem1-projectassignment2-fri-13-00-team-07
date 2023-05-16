package game;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class SettingManager {
    private MapReader mapReader;
    private PropertyReader propertyReader;
    private ItemManager itemManager;

    public SettingManager(Properties properties, PacManMap map) {
        propertyReader = new PropertyReader(properties);
        if (map instanceof EditorMap)
            mapReader = new EditorMapReader((EditorMap) map);
        else
            mapReader = new PropertyMapReader((PacManGameGrid) map, propertyReader);

        itemManager = new ItemManager(mapReader.getItemLocations());
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

    /* Wrapper methods using delegation */

    public HashMap<Location, ActorType> getItemLocations() {
        return mapReader.getItemLocations();
    }

    public HashMap<Location, ActorType> getCharacterLocations() {
        return mapReader.getCharacterLocations();
    }

    public int getSeed() {
        return propertyReader.getSeed();
    }

    public void drawSetting(Level level) {
        itemManager.drawSetting(level);
    }

    public int countPills() {
        return itemManager.countPills();
    }

    public boolean getPlayerMode() {
        return propertyReader.readBoolean("PacMan.isAuto");
    }

    public ArrayList<String> getPlayerMoves() {
        return propertyReader.readMoves("PacMan.move");
    }

    public Location closestItemLocation(Location target) {
        return itemManager.closestItemLocation(target);
    }

    public Item getItem(Location atLocation) { return itemManager.getItem(atLocation); }

    public void removeItem(Location location) { itemManager.removeItem(location); }
}
