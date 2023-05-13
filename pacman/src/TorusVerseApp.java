import java.io.File;

public class TorusVerseApp {
    // default EDIT mode
    private AppMode mode = AppMode.EDIT;
    private GameAdapter gameAdapter;
    private EditorAdapter editorAdapter;


    private TorusVerseApp() {
        gameAdapter = AppComponentFactory.getInstance().getGameAdapter();
        editorAdapter = AppComponentFactory.getInstance().getEditorAdapter();
    }

    public TorusVerseApp(String dir) {
        this();
        // keep the editor in the background
        editorAdapter.runEditor(null);

        File file = new File(dir);
        if (file.isDirectory()) {
            mode = AppMode.TEST;
            gameAdapter.runGame(dir);
        } else if (file.isFile()) { // map file passed
            editorAdapter.runEditor(dir);
        }
        // returning to edit mode with no current map
        mode = AppMode.EDIT;
        editorAdapter.runEditor(dir);
    }
}
