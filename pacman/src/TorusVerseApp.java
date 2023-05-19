import checker.GameChecker;
import checker.LevelChecker;
import game.Game;
import game.Maps.EditorMap;
import game.Maps.PacManMap;

import java.io.File;
import java.util.*;

public class TorusVerseApp {
    // default EDIT mode
    private AppMode mode = AppMode.EDIT;
    private Game game;
    private EditorAdapter editorAdapter;

    public TorusVerseApp(String dir) { // given folder or file
        editorAdapter = AppComponentFactory.getInstance().getEditorAdapter();
        if (dir == null) { // edit mode with no current map
            editorAdapter.runEditor(null);
            return;
        }

        File file = new File(dir);
        if (file.isDirectory()) {
            mode = AppMode.TEST;
            GameChecker gameChecker = GameChecker.getInstance();
            ArrayList<String> validFiles = new ArrayList<>();
            if (gameChecker.checkGame(dir)) {
                validFiles.addAll(gameChecker.getValidMapFiles());
                Collections.sort(validFiles);
            }

            ArrayList<PacManMap> maps = new ArrayList<>();
            for (String f : validFiles) {
                PacManMap map = getMap(dir + "/" + f);
                if (!LevelChecker.getInstance().checkLevel((EditorMap) map)) { // can always cast, as it is a xml
                    mode = AppMode.EDIT;
                    editorAdapter.runEditor(dir + "/" + f);
                    return;
                }
                maps.add(map);
            }

            game = new Game(maps);
        } else {
            // returning to edit mode with no current map
            mode = AppMode.EDIT;
            editorAdapter.setMap(dir);
            editorAdapter.runEditor(dir);
        }
    }

    /**
     * Gets the map from the given map file name.
     * @param mapFile
     * @return
     */
    private PacManMap getMap(String mapFile) {
        return new EditorMap(mapFile, editorAdapter.getMap(mapFile));
    }

    private boolean isMap(File f) {
        return Character.isDigit(f.getName().charAt(0)) && f.getName().endsWith(".xml");
    }
}
