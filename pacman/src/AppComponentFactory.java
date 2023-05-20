import checker.*;
import game.Game;
import game.Maps.PacManMap;

import java.util.ArrayList;

/**
 * A singleton factory for producing the app's components,
 * such as the editor adapter.
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

    public Checker getChecker(CheckerType checkerType) {
        if (checkerType.equals(CheckerType.GAME_CHECKER))
            return new GameChecker();

        return new LevelChecker(); // default
    }

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
