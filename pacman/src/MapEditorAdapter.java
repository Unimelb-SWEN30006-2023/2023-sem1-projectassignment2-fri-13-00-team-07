import matachi.mapeditor.editor.Controller;

public class MapEditorAdapter implements EditorAdapter {
    private Controller editor;

    @Override
    public void runEditor(String mapFile) {
        editor = new Controller(mapFile); // if null, then no current map
    }
}
