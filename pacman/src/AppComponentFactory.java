/**
 * A singleton factory for producing the app's components,
 * such as the editor adapter.
 */
public class AppComponentFactory {
    private EditorAdapter editorAdapter = null;
    private static AppComponentFactory instance = null; // singleton

    /**
     * Gets an editor adapter.
     * @return an editor adapter
     */
    public EditorAdapter getEditorAdapter() {
        if (editorAdapter == null)
            editorAdapter = new MapEditorAdapter(); // lazy creation
        return editorAdapter;
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
