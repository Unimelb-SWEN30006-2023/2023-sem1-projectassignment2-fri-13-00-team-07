package game;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;
import game.Items.Item;
import game.Items.ItemManager;
import game.Maps.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class SettingManager implements PacManMap {
    private final MapReader mapReader;
    private final PropertyReader propertyReader;
    private final ItemManager itemManager;

    public SettingManager(Properties properties, PacManMap map, Level level) {
        propertyReader = new PropertyReader(properties);
        if (map instanceof EditorMap)
            mapReader = new EditorMapReader((EditorMap) map);
        else
            mapReader = new PropertyMapReader((PacManGameGrid) map, propertyReader);

        itemManager = new ItemManager(mapReader.getItemLocations(), map.getHorizontalCellsCount(), map.getVerticalCellsCount(), level);
    }


    /**
     * Checks whether the given location is not a wall.
     * @param location: location to be checked
     * @return true if it's a wall, false otherwise.
     */
    public boolean isWallAt(Location location) {
        return isInBound(location) && itemManager.isWallAt(location);
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

    @Override
    public CellType getTypeAt(Location location) {
        Item item = getItem(location);
        return item == null ? null : item.getType();
    }

    @Override
    public int getHorizontalCellsCount() {
        return itemManager.getHorizontalCellsCount();
    }

    @Override
    public int getVerticalCellsCount() {
        return itemManager.getVerticalCellsCount();
    }
}
