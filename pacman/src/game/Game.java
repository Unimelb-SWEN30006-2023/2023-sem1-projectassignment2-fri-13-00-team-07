package game;

import game.Maps.PacManMap;
import game.utility.PropertiesLoader;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

public class Game {
    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test2.properties";
    private final ArrayList<Level> levels = new ArrayList<>();

    // same as original - creates a game with a single level
    public Game() {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties));
        run();
    }

    public Game(PacManMap map) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties, map, Optional.empty())); // single level
        run();
    }

    public Game(ArrayList<PacManMap> maps) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        for (PacManMap map : maps) {
            levels.add(new Level(properties, map, Optional.empty()));
        }
        run();
    }

    public void run() {
        for (Level level : levels)
            level.run();
    }
}
