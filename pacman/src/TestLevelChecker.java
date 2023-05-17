import checker.LevelChecker;
import game.Maps.EditorMap;

public class TestLevelChecker {

    public static void main(String[] args){
        String dir = "pacman/data/2_ErrorMap.xml";
        EditorAdapter editorAdapter = AppComponentFactory.getInstance().getEditorAdapter();
        editorAdapter.runEditor(dir);
        EditorMap editorMap = new EditorMap(dir, editorAdapter.getMap(dir));
        LevelChecker levelChecker = LevelChecker.getInstance();
        if(levelChecker.checkLevel(editorMap)){
            System.out.println("Level Checker test: all test passed");
        }
        else{
            System.out.println("Level Checker test: some test failed");
        }
    }
}