package game;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;

public class PropertySettingManager extends SettingManager {
    public PropertySettingManager(PacManGameGrid grid) {
        super(grid);
    }

    private boolean useMazePillLocations, useMazeGoldLocations;
    /**
     * Sets up the setting for the level.
     * @param level: level to be set up.
     */
    @Override
    public void draw(Level level) {
        // single use lists, no need to store
        ArrayList<Location> propertyPillLocations = level.getPropertyReader().loadLocations("Pills.location");
        ArrayList<Location> propertyGoldLocations = level.getPropertyReader().loadLocations("Gold.location");

        // empty property locations -> use maze locations
        useMazePillLocations = (propertyPillLocations.size() == 0);
        useMazeGoldLocations = (propertyGoldLocations.size() == 0);

        super.draw(level);

        // does nothing if the property locations are empty
        putPropertiesPills(level, propertyPillLocations, propertyGoldLocations);
    }

    /**
     * Puts pills (including gold) according to the locations in the property file.
     * @param level: level to be set up
     * @param propertyPillLocations: ArrayList of pill locations from the properties file
     * @param propertyGoldLocations: ArrayList of gold locations from the properties file
     */
    private void putPropertiesPills(Level level, ArrayList<Location> propertyPillLocations,
                                    ArrayList<Location> propertyGoldLocations) {
        for (Location location : propertyPillLocations)
            putItem(level, location, new Pill());

        for (Location location : propertyGoldLocations)
            putItem(level, location, new Gold());
    }

    /**
     * Factory method to create the item corresponding to the given cell type.
     * @return the required item is the cellType if valid, null otherwise
     */
    @Override
    public Item createItem(CellType cellType) {
        if (cellType.equals(CellType.PILL) && useMazePillLocations) {
            return new Pill();
        } else if (cellType.equals(CellType.GOLD) && useMazeGoldLocations) {
            return new Gold();
        } else if (cellType.equals(CellType.ICE)) {
            return new IceCube();
        }
        return null;
    }
}
