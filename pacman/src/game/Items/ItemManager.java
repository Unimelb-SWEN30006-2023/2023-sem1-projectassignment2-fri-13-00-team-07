package game.Items;

import ch.aplu.jgamegrid.*;
import game.ActorType;
import game.Level;

import java.util.*;

/**
 * Manages the setting for a game.
 * Connects the game and the grid.
 */

public class ItemManager {
    // for initialization
    private HashMap<Location, ActorType> itemLocations;
    /* Maps the index of a location in the grid to an item
     * Cannot use location directly as the key, as Location does not implement hashCode correctly.
     */
    // for later lookups
    private final HashMap<Integer, Item> items = new HashMap<>();

    public ItemManager(HashMap<Location, ActorType> itemLocations) {
        this.itemLocations = itemLocations;
    }

    /**
     * Draws the items from the locations stored.
     * Precondition: The portals must be in pairs.
     */
    public void drawSetting(Level level) {
        GGBackground bg = level.getBg();

        HashMap<CellType, ArrayList<Location>> portalLocations = new HashMap<>();

        for (Map.Entry<Location, ActorType> entry : itemLocations.entrySet()) {
            Location location = entry.getKey();
            ActorType cellType = entry.getValue();

            if (CellType.Portals().contains(cellType)) {
                portalLocations.computeIfAbsent(cellType, k -> new ArrayList<>());
                portalLocations.get(cellType).add(location);
            } else {
                colorWallAndSpace(location, cellType, bg);
                Item item = createItem(cellType);
                if (item != null) {
                    putItem(location, item, level);
                }
            }
        }

        for (final var entry: portalLocations.entrySet()) {
            putItem(entry.getValue().get(0), new Portal(entry.getKey(), entry.getValue().get(1)), level);
            putItem(entry.getValue().get(1), new Portal(entry.getKey(), entry.getValue().get(0)), level);
        }
    }


    /**
     * Colors the cell at the given location to a space by default.
     * If the cell is a wall, colors it as a wall.
     * @param location: location of the cell to be colored
     */
    private void colorWallAndSpace(Location location, ActorType cellType, GGBackground bg) {
        if (cellType.equals(CellType.WALL)) {
            bg.fillCell(location, CellType.WALL.getColor());
        } else {
            // fill with the space's color by default
            bg.fillCell(location, CellType.SPACE.getColor());
        }
    }

    /**
     * Factory method to create the item corresponding to the given cell type.
     * @return the required item is the cellType if valid, null otherwise
     */
    private Item createItem(ActorType cellType) {
        if (CellType.PILL.equals(cellType)) {
            return new Pill();
        } else if (CellType.GOLD.equals(cellType)) {
            return new Gold();
        } else if (CellType.ICE.equals(cellType)) {
            return new IceCube();
        }
        return null;
    }

    /**
     * Puts the given item at the given location in the level.
     * @param location: the location for the item
     * @param item: the item to put
     */
    protected void putItem(Location location, Item item, Level level) {
        level.getBg().setPaintColor(item.getColor());
        level.getBg().fillCircle(level.toPoint(location), Item.getFillCircleRadius());

        items.put(getIndexByLocation(location), item);
        level.addActor(item, location);
    }

    /**
     * Returns the closest item location to the given location.
     * @param target: the target location
     * @return the closest location to the target.
     */
    public Location closestItemLocation(Location target) {
        int currentMinDistance = Integer.MAX_VALUE;
        Location currentClosestLocation = null;

        for (Map.Entry<Integer, Item> entry: items.entrySet()) {
            Item item = entry.getValue();
            if (item.getType() != CellType.PILL && !item.isVisible())
                continue; // already removed

            Location itemLocation = getLocationByIndex(entry.getKey());
            int distanceToItem = target.getDistanceTo(itemLocation);

            if (distanceToItem < currentMinDistance) {
                currentClosestLocation = itemLocation;
                currentMinDistance = distanceToItem;
            }
        }

        return currentClosestLocation;
    }


    /**
     * Removes the item at the given location.
     * (Only plain pills are explicitly removed, other items are just hidden.)
     * @param location: location of the item to remove
     */
    public void removeItem(Location location) {
        Item item = getItem(location);
        if (item == null)
            return;

        // recolor the cell to a space
        item.getBackground().fillCell(item.getLocation(), CellType.SPACE.getColor());
        item.hide();

        // need to explicitly remove the pill
        // as hide() is ineffective for actors with no sprite
        if (item.getType().equals(CellType.PILL)) {
            items.remove(getIndexByLocation(location));
        }
    }

    /**
     * Gets the item at the specified location.
     * @param location: the target location
     * @return the item at the given location,
     *         null if the item is hidden or not found.
     */
    public Item getItem(Location location) {
        Item item = items.get(getIndexByLocation(location));
        if (item != null && item.getType() != CellType.PILL && !item.isVisible()) {
            // 'removed' (hidden) gold or ice
            return null;
        }
        return item;
    }


    /**
     * Counts the number of pills (including golds).
     * @return the number of pills in the grid.
     */
    public int countPills() {
        return (int) items.values().stream()
                          .filter(i ->  i instanceof Pill)
                          .count();
    }

    /**
     *
     * For Orion, get a dictionary of locations and the gold there.
     */
    /**
     * Gets the list of gold locations.
     * @return a list of all gold locations.
     */
    public List<Location> getGoldLocations() { // for Orion
        return items.entrySet().stream()
                    .filter(i -> i.getValue() instanceof Gold)
                    .map(i -> getLocationByIndex(i.getKey()))
                    .toList();
    }

    /**
     * Gets the location corresponding to the given index.
     * @param index: an integer specifying the index in the game grid
     * @return the corresponding location.
     */
    private Location getLocationByIndex(int index) {
        return new Location(index % Level.getNumHorzCells(), index / Level.getNumHorzCells());
    }

    /**
     * Gets the index corresponding to the given location.
     * @param location: location to look up
     * @return the corresponding index in the game grid.
     */
    private int getIndexByLocation(Location location) {
        return location.y * Level.getNumHorzCells() + location.x;
    }

}
