public class AppComponentFactory {
    private GameAdapter gameAdapter = null;
    private EditorAdapter editorAdapter = null;
    private static AppComponentFactory instance = null; // singleton

    /* Logic to be added for future extension */
    public GameAdapter getGameAdapter() { // lazy creation on request
        if (gameAdapter == null)
            gameAdapter = new PacManGameAdapter();
        return gameAdapter;
    }

    public EditorAdapter getEditorAdapter() {
        if (editorAdapter == null)
            editorAdapter = new MapEditorAdapter();
        return editorAdapter;
    }

    public static AppComponentFactory getInstance() {
        if (instance == null)
            instance = new AppComponentFactory();
        return instance;
    }
}
