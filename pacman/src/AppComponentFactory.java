import checker.*;
import checker.gameChecks.GameChecker;
import checker.levelChecks.CompositeLevelChecker;
import game.Game;
import game.Maps.PacManMap;

import java.util.ArrayList;

/**
 * A singleton factory for producing the app's components,
 * including the game, editor adapter, and checkers.
 */
public class AppComponentFactory {
    private static AppComponentFactory instance = null; // singleton

    /**
     * Gets an editor adapter.
     * @return an editor adapter
     */
    public EditorAdapter getEditorAdapter() {
        return new MapEditorAdapter(); // lazy creation
    }

    /**
     * Gets the required type of checker.
     * @param checkerType: type of checker
     * @return either a game or level checker, as required.
     */

    public Checker getChecker(CheckerType checkerType) {
        if (checkerType.equals(CheckerType.GAME_CHECKER))
            return new GameChecker();

        // gives a LevelChecker by default
        return new CompositeLevelChecker();
    }

    /**
     * Gets the Game.
     * @param maps: ArrayList of PacManMaps to be used by the Game.
     * @return: the Game created based on the maps.
     */
    public Game getGame(ArrayList<PacManMap> maps) {
        return new Game(maps);
    }

    /**
     * Gets the single instance of this factory.
     * @return the AppComponentFactory instance.
     */
    public static AppComponentFactory getInstance() {
        if (instance == null)
            instance = new AppComponentFactory();
        return instance;
    }
}
