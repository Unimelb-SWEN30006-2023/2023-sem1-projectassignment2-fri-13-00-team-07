import mapeditor.editor.Controller;
import org.jdom.JDOMException;

import java.io.IOException;

public class MapEditorAdapter implements EditorAdapter {

    public MapEditorAdapter() { }

    @Override
    public void runEditor(String mapFile) throws IOException, JDOMException {
        final Controller editorController = new Controller(mapFile);
    }

    @Override
    public boolean applyLevelCheck() {
        // FIXME: implementation
        return false;
    }
}
