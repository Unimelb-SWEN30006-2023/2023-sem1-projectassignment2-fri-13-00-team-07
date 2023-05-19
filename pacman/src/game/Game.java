package game;

import game.Maps.PacManMap;
import game.utility.PropertiesLoader;

import java.lang.ref.WeakReference;
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
        levels.add(new Level(properties, map, Optional.empty(), Optional.of(new WeakReference<>(this)))); // single level
        run();
    }

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

    public void run() {
        runNext();
    }

    public void runNext() {
        if (!levels.isEmpty()) {
            levels.remove(0).run();
        }
    }
}
