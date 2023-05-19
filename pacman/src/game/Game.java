package game;

import game.Maps.PacManMap;
import game.utility.PropertiesLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

/**
 * The top level game, which contains a set of levels.
 */
public class Game {

    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test2.properties";
    private final ArrayList<Level> levels = new ArrayList<>();


    /**
     * Creates a game with the single default level.
     * same as original - creates a game with a single level
     */
    public Game() {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties));
        run();
    }

    /**
     * Creates a game with a given map, which is its only level.
     *
     * @param map The map of a level.
     */
    public Game(PacManMap map) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties, map, Optional.empty(), Optional.of(new WeakReference<>(this)))); // single level
        run();
    }

    /**
     * Creates a game with multiple maps, each of which maps to a level.
     *
     * @param maps The maps, each of which represents a level.
     */
    public Game(ArrayList<PacManMap> maps) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        for (PacManMap map : maps) {
            levels.add(new Level(properties, map, Optional.of(new LevelCompletionHandler() {
                @Override
                public void handler(Game game) {
                    game.runNext();
                }
            }), Optional.of(new WeakReference<>(this))));
        }
        run();
    }

    /**
     * Starts the game.
     */
    public void run() {
        runNext();
    }

    private void runNext() {
        if (!levels.isEmpty()) {
            levels.remove(0).run();
        }
    }

    /**
     * @return Whether there is no other levels left.
     */
    public boolean isLevelsEmpty() {
        return levels.isEmpty();
    }
}
