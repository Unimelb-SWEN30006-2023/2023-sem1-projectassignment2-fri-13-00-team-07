package game;

import game.utility.PropertiesLoader;

import java.util.ArrayList;
import java.util.Properties;

public class Game {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/test2.properties";
    private ArrayList<Level> levels = new ArrayList<>();

    // same as original - creates a game with a single level
    public Game() {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties));
        run();
    }

    public Game(PacManGameGrid grid) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        levels.add(new Level(properties, grid)); // single level
        run();
    }

    public Game(ArrayList<PacManGameGrid> grids) {
        Properties properties = PropertiesLoader.loadPropertiesFile(DEFAULT_PROPERTIES_PATH);
        for (PacManGameGrid grid : grids) {
            levels.add(new Level(properties, grid));
        }
        run();
    }

    public void run() {
        for (Level level : levels)
            level.run();
    }
}
