package TestPrograms;

import checker.CheckerType;
import checker.levelChecks.CompositeLevelChecker;
import game.Maps.EditorMap;
import mainApp.AppComponentFactory;
import mainApp.EditorAdapter;
import org.jdom.JDOMException;

import java.io.IOException;

/**
 * Tests for a CompositeLevelChecker - not part of the required
 * functionalities for this project.
 */
public class TestLevelChecker {

    public static void main(String[] args) {
        try {
            String dir = "pacman/data/crackLevelChecker/1_noPacStart_badPortals.xml";
            EditorAdapter editorAdapter = AppComponentFactory.getInstance().getEditorAdapter();
            editorAdapter.runEditor(dir);
            EditorMap editorMap = new EditorMap(dir);
            CompositeLevelChecker compositeLevelChecker = (CompositeLevelChecker) AppComponentFactory.getInstance().getChecker(CheckerType.LEVEL_CHECKER);
            if (compositeLevelChecker.check(editorMap)) {
                System.out.println("Level Checker test: all test passed");
            } else {
                System.out.println("Level Checker test: some test failed");
            }
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }
    }
}
