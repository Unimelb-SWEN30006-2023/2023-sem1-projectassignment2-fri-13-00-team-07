package game;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Responsible for reading and parsing locations from the properties file.
 */

public class PropertyReader {

    private final Properties properties;

    /**
     * Creates a property reader for the given properties file
     * @param properties: the properties file to be read
     */
    public PropertyReader(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets the property according to the key.
     * @param key: key (String) to look up in the properties file
     * @return the corresponding value in the properties file.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Load a series of locations from the properties file
     * corresponding to the given key.
     * @param key: key (String) to look up in the properties file
     * @return an ArrayList of all corresponding locations.
     */
    public ArrayList<Location> loadLocations(String key) {
        ArrayList<Location> locations = new ArrayList<>();

        String propertyLocationsString = properties.getProperty(key);
        if (propertyLocationsString != null) {
            String[] singleLocationStrings = propertyLocationsString.split(";");
            for (String singleLocationString: singleLocationStrings) {
                locations.add(parseLocation(singleLocationString));
            }
        }

        return locations;
    }

    /**
     * Reads and parses a single location.
     * @param key: key (String) to look up in the properties file
     * @return: the corresponding location.
     */
    public Location readLocation(String key) {
        String locationString = properties.getProperty(key);
        return parseLocation(locationString);
    }

    /**
     * Parse the String array to a proper location.
     * @param locationString: a String array of the form "x, y"
     * @return the corresponding location.
     */
    private Location parseLocation(String locationString) {
        String[] coordinates = locationString.split(",");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        return new Location(x, y);
    }

    /**
     * Reads and parses a boolean value.
     * @param key: key (String) to look up in the properties file
     * @return: the corresponding boolean value.
     */
    public boolean readBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    /**
     * Reads and parses the moves.
     * @param key: key (String) to look up in the properties file
     * @return: the corresponding moves, as an ArrayList of Strings
     *          (an empty list if there's no corresponding value).
     */
    public ArrayList<String> readMoves(String key) {
        String propertyMoveString = properties.getProperty(key);
        ArrayList<String> moves = new ArrayList<>();
        if (propertyMoveString != null)
            moves.addAll(List.of(propertyMoveString.split(",")));

        return moves;
    }

    /**
     * Gets the seed.
     * @return the integer seed.
     */
    public int getSeed() {
        return Integer.parseInt(properties.getProperty("seed"));
    }
}
