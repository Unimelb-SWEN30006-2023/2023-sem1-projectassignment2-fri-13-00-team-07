package mainApp;

import mainApp.EditorAdapter;
import mapeditor.editor.Controller;
import org.jdom.JDOMException;

import java.io.IOException;

/**
 * An adapter for the 2D Map Editor created by Matachi.
 */

public class MapEditorAdapter implements EditorAdapter {

    /**
     * @inheritDoc
     */
    @Override
    public void runEditor(String filePath) throws IOException, JDOMException {
        if (filePath == null)
            new Controller();
        else
            new Controller(filePath);
    }
}
