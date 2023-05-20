import checker.CheckerType;
import checker.GameChecker;
import checker.LevelChecker;
import game.Maps.EditorMap;
import game.Maps.PacManMap;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TorusVerseApp {
    // default EDIT mode
    private AppMode mode = AppMode.EDIT;

    public TorusVerseApp(String dir) throws IOException, JDOMException { // given folder or file
        AppComponentFactory factory = AppComponentFactory.getInstance();
        EditorAdapter editorAdapter = factory.getEditorAdapter();
        if (dir == null) { // edit mode with no current map
            editorAdapter.runEditor(null);
            return;
        }

        File file = new File(dir);
        if (file.isDirectory()) {
            mode = AppMode.TEST;
            GameChecker gameChecker = (GameChecker) factory.getChecker(CheckerType.GAME_CHECKER);
            if (gameChecker.checkGame(dir)) {
                ArrayList<String> validFiles = gameChecker.getValidMapFiles();
                Collections.sort(validFiles);

                ArrayList<PacManMap> maps = new ArrayList<>();
                for (String f : validFiles) {
                    EditorMap map = new EditorMap(dir + "/" + f);

                    LevelChecker levelChecker = (LevelChecker) factory.getChecker(CheckerType.LEVEL_CHECKER);
                    if (!levelChecker.checkLevel(map)) { // can always cast, as it is a xml
                        mode = AppMode.EDIT;
                        editorAdapter.runEditor(dir + "/" + f);
                        return;
                    }
                    maps.add(map);
                }

                factory.getGame(maps);
            } else {
                throw new IOException("Game check failed");
            }
        } else {
            mode = AppMode.EDIT;
            editorAdapter.runEditor(dir);
        }
    }

}
