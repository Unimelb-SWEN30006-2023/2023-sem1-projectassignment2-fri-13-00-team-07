import checker.LevelChecker;
import game.Maps.EditorMap;
import org.jdom.JDOMException;

import java.io.IOException;

public class TestLevelChecker {

    public static void main(String[] args) {
        try {
            String dir = "pacman/data/crackLevelChecker/1_noPacStart_badPortals.xml";
            EditorAdapter editorAdapter = AppComponentFactory.getInstance().getEditorAdapter();
            editorAdapter.runEditor(dir);
            EditorMap editorMap = new EditorMap(dir);
            LevelChecker levelChecker = LevelChecker.getInstance();
            if (levelChecker.checkLevel(editorMap)) {
                System.out.println("Level Checker test: all test passed");
            } else {
                System.out.println("Level Checker test: some test failed");
            }
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }
    }
}
