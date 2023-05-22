package game.Workers;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.Item;
import game.Level;
import game.Maps.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * The executive manager amongst the workers.
 * Allocate jobs to the appropriate worker by providing a series of
 * 'wrapper' methods.
 */
public class SettingManager {
    private final MapReader mapReader;
    private final PropertyReader propertyReader;
    private final ItemManager itemManager;
    private PacManMap map;

    /**
     * Constructs a SettingManager.
     * @param properties: properties file for the level
     * @param map: map on which the level is based
     * @param level: game level to be managed by the setting manager.
     */
    public SettingManager(Properties properties, PacManMap map, Level level) {
        this.map = map;
        propertyReader = new PropertyReader(properties);
        if (map instanceof EditorMap)
            mapReader = new EditorMapReader();
        else
            mapReader = new PropertyMapReader(propertyReader);

        itemManager = new ItemManager(map, level);
    }

    /* Wrapper methods using delegation */

    /**
     * Checks whether the given location is not a wall.
     * @param location: location to be checked
     * @return true if it's a wall, false otherwise.
     */
    public boolean isWallAt(Location location) {
        return map.isInBound(location) && itemManager.isWallAt(location);
    }

    /**
     * Gets the item manager.
     * @return the ItemManager.
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Gets the item locations from the map.
     * @return the HashMap containing the items' locations.
     */
    public HashMap<Integer, ActorType> getItemLocations() {
        return mapReader.getItemLocations(map);
    }

    /**
     * Gets the character locations from the map.
     * @return the HashMap containing the characters' (MovingActors) locations.
     */
    public HashMap<Integer, ActorType> getCharacterLocations() {
        return mapReader.getCharacterLocations(map);
    }

    /**
     * Gets the seed from the property reader.
     * @return the integer seed.
     */
    public int getSeed() {
        return propertyReader.getSeed();
    }

    /**
     * Gets the count of pills from the item manager.
     * @return the number of remaining pills.
     */
    public int countPills() {
        return itemManager.countPills();
    }

    /**
     * Gets the player mode from the property reader.
     * @return the boolean value representing whether the player mode is auto.
     */
    public boolean getPlayerMode() {
        return propertyReader.readBoolean("PacMan.isAuto");
    }

    /**
     * Gets the player's moves from the property reader.
     * @return an ArrayList of Strings representing the PacPlayer's property moves.
     */
    public ArrayList<String> getPlayerMoves() {
        return propertyReader.readMoves("PacMan.move");
    }

    /**
     * Gets the item at the given location from the item manager.
     * @param location: location to be looked up
     * @return the item at that location.
     */
    public Item getItem(Location location) { return itemManager.getItem(location); }

    /**
     * Removes the item at the given location (via the item manager).
     * @param location: location of the item to be removed.
     */
    public void removeItem(Location location) { itemManager.removeItem(location); }

    /** {@inheritDoc} */
    public int getHorizontalCellsCount() {
        return itemManager.getHorizontalCellsCount();
    }

    /** {@inheritDoc} */
    public int getVerticalCellsCount() {
        return itemManager.getVerticalCellsCount();
    }

    /**
     * Gets the map.
     * @return a PacManMap.
     */
    public PacManMap getMap() {
        return map;
    }

    /**
     * Checks if the location is in-bound on the map.
     * @param location: location to be checked
     * @return true if the location is in-bound, false otherwise.
     */
    public boolean isInBound(Location location) {
        return map.isInBound(location);
    }

}
