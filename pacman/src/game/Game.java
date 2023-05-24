package game;

import game.Maps.PacManMap;
import game.utility.PropertiesLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

/**
 * The top-level game, which contains a set of levels.
 * The facade class for the game component of the app.
 */
public class Game {

    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test.properties";
    private final ArrayList<Level> levels = new ArrayList<>();


    /**
     * Creates a game with the single default level.
     * (Same as the original game's behavior.)
     */
    public Game() {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties));
        run();
    }

    /**
     * Creates a game with a single level from the given map.
     * @param map The map for the level.
     */
    public Game(PacManMap map) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties, map, Optional.empty(), Optional.of(new WeakReference<>(this)))); // single level
        run();
    }

    /**
     * Creates a game with multiple maps, each of which corresponds to a level.
     * @param maps: an ArrayList of maps, each of which represents a level.
     */
    public Game(ArrayList<PacManMap> maps) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        for (PacManMap map : maps) {
            // add the completionHandler for each level
            levels.add(new Level(properties, map, Optional.of(new LevelCompletionHandler() {
                @Override
                public void handleCompletion(Game game) {
                    game.runNext();
                }
            }), Optional.of(new WeakReference<>(this))));
        }
        run();
    }

    /**
     * Starts and runs the game.
     */
    public void run() {
        runNext();
    }

    /**
     * Runs the next level of the game (if any).
     */
    private void runNext() {
        if (!levels.isEmpty()) {
            // pop and run
            levels.remove(0).run();
        }
    }

    /**
     * Checks if there are any more levels left to run.
     * @return true if there are no more levels, false otherwise.
     */
    public boolean noMoreLevels() {
        return levels.isEmpty();
    }
}
