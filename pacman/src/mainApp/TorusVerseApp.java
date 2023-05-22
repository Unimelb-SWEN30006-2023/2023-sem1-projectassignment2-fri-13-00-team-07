package mainApp;

import checker.CheckerType;
import checker.levelChecks.CompositeLevelChecker;
import checker.gameChecks.GameChecker;
import game.Maps.EditorMap;
import game.Maps.PacManMap;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TorusVerseApp {

    /**
     * Constructs and runs the TorusVerse Application with no current map.
     * @throws IOException
     * @throws JDOMException
     */
    public TorusVerseApp() throws IOException, JDOMException {
        this(null);
    }

    /**
     * Constructs and runs the TorusVerse Application.
     * @param dir: directory or file path, on which the app is to be built.
     * @throws IOException
     * @throws JDOMException
     */
    public TorusVerseApp(String dir) throws IOException, JDOMException { // given folder or file
        if (dir == null) { // edit mode with no current map
            runEditMode(null);
            return;
        }

        File file = new File(dir);
        if (file.isDirectory()) {
            runTestMode(dir);
        } else {
            runEditMode(dir);
        }
    }

    /**
     * Runs the test mode of the app.
     * @param dir: directory to the map files on which the tester is to run.
     * @throws IOException
     * @throws JDOMException
     */
    private void runTestMode(String dir) throws IOException, JDOMException {
        AppComponentFactory factory = AppComponentFactory.getInstance();
        GameChecker gameChecker = (GameChecker) factory.getChecker(CheckerType.GAME_CHECKER);

        if (gameChecker.check(dir)) { // checking succeeded
            ArrayList<String> validFiles = gameChecker.getValidMapFiles();
            Collections.sort(validFiles);

            ArrayList<PacManMap> maps = new ArrayList<>(); // valid maps
            for (String f : validFiles) {
                EditorMap map = new EditorMap(dir + "/" + f);

                CompositeLevelChecker compositeLevelChecker =
                        (CompositeLevelChecker) factory.getChecker(CheckerType.LEVEL_CHECKER);
                if (!compositeLevelChecker.check(map)) {
                    // checking failed, return to edit mode on that map
                    runEditMode(dir + "/" + f);
                    return;
                }
                maps.add(map);
            }

            // all good now -> build and run the game based on the maps
            factory.getGame(maps);

        } else {
            throw new IOException("Game check failed");
        }
    }

    /**
     * Runs the edit mode of the app.
     * @param dir: path to the map file on which the editor is to run.
     * @throws IOException
     * @throws JDOMException
     */
    private void runEditMode(String dir) throws IOException, JDOMException {
        EditorAdapter editorAdapter = AppComponentFactory.getInstance().getEditorAdapter();
        editorAdapter.runEditor(dir);
    }
}
