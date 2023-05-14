public class AppComponentFactory {
    private EditorAdapter editorAdapter = null;
    private static AppComponentFactory instance = null; // singleton

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
