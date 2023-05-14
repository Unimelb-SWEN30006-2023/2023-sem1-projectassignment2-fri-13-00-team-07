package game;

import ch.aplu.jgamegrid.*;

import java.util.*;

/**
 * Manages the setting for a game.
 * Connects the game and the grid.
 */

public class SettingManager {
    /* Maps the index of a location in the grid to an item
     * Cannot use location directly as the key, as Location does not implement hashCode correctly.
     */
    private final HashMap<Integer, Item> items = new HashMap<>();
    private final PacManGameGrid grid = new PacManGameGrid();

    /**
     * Sets up the setting for the game.
     * @param game: game to be set up.
     */
    public void drawMap(Game game) {
        // single use lists, no need to store
        ArrayList<Location> propertyPillLocations = game.getPropertyReader().loadLocations("Pills.location");
        ArrayList<Location> propertyGoldLocations = game.getPropertyReader().loadLocations("Gold.location");

        // empty property locations -> use maze locations
        final boolean useMazePillLocations = (propertyPillLocations.size() == 0);
        final boolean useMazeGoldLocations = (propertyGoldLocations.size() == 0);

        for (int y = 0; y < Game.getNumVertCells(); y++) {
            for (int x = 0; x < Game.getNumHorzCells(); x++) {
                Location location = new Location(x, y);
                CellType cellType = grid.getCellType(location);
                GGBackground bg = game.getBg();

                colorWallAndSpace(location, bg);

                Item item = createItem(cellType, useMazePillLocations, useMazeGoldLocations);
                if (item != null) {
                    putItem(game, location, item);
                }
            }
        }

        // does nothing if the property locations are empty
        putPropertiesPills(game, propertyPillLocations, propertyGoldLocations);
    }

    /**
     * Colors the cell at the given location to a space by default.
     * If the cell is a wall, colors it as a wall.
     * @param location: location of the cell to be colored
     * @param bg: background consisting of the cells
     */
    private void colorWallAndSpace(Location location, GGBackground bg) {
        CellType cellType = grid.getCellType(location);
        if (cellType.equals(CellType.WALL)) {
            bg.fillCell(location, CellType.WALL.getColor());
        } else {
            // fill with the space's color by default
            bg.fillCell(location, CellType.SPACE.getColor());
        }
    }

    /**
     * Puts pills (including gold) according to the locations in the property file.
     * @param game: game to be set up
     * @param propertyPillLocations: ArrayList of pill locations from the properties file
     * @param propertyGoldLocations: ArrayList of gold locations from the properties file
     */
    private void putPropertiesPills(Game game, ArrayList<Location> propertyPillLocations,
                                    ArrayList<Location> propertyGoldLocations) {
        for (Location location : propertyPillLocations)
            putItem(game, location, new Pill());

        for (Location location : propertyGoldLocations)
            putItem(game, location, new Gold());
    }

    /**
     * Factory method to create the item corresponding to the given cell type.
     * @return the required item is the cellType if valid, null otherwise
     */
    private Item createItem(CellType cellType, boolean useMazePillLocations, boolean useMazeGoldLocations) {
        if (cellType.equals(CellType.PILL) && useMazePillLocations) {
            return new Pill();
        } else if (cellType.equals(CellType.GOLD) && useMazeGoldLocations) {
            return new Gold();
        } else if (cellType.equals(CellType.ICE)) {
            return new IceCube();
        }
        return null;
    }

    /**
     * Puts the given item at the given location in the game.
     * @param game: the current game
     * @param location: the location for the item
     * @param item: the item to put
     */
    private void putItem(Game game, Location location, Item item) {
        game.getBg().setPaintColor(item.getColor());
        game.getBg().fillCircle(game.toPoint(location), Item.getFillCircleRadius());

        items.put(getIndexByLocation(location), item);
        game.addActor(item, location);
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
        return new Location(index % Game.getNumHorzCells(), index / Game.getNumHorzCells());
    }

    /**
     * Gets the index corresponding to the given location.
     * @param location: location to look up
     * @return the corresponding index in the game grid.
     */
    private int getIndexByLocation(Location location) {
        return location.y * Game.getNumHorzCells() + location.x;
    }

    /**
     * Checks whether the given location is not a wall.
     * @param location: location to be checked
     * @return true if it's a wall, false otherwise.
     */
    public boolean isWallAt(Location location) {
        return grid.isInBound(location) && grid.getCellType(location) == CellType.WALL;
    }

    /**
     * Checks if the location is in bound of the grid.
     * @param location: location to be checked
     * @return true if it's in bound, false otherwise.
     */
    public boolean isInBound(Location location) {
        return grid.isInBound(location);
    }
}
