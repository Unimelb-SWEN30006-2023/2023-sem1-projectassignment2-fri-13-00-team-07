import ch.aplu.jgamegrid.Location;
import game.Maps.EditorMap;
import mapeditor.editor.Controller;
import org.jdom.JDOMException;

import java.io.IOException;

/**
 * An adapter for the 2D Map Editor created by Matachi.
 */

public class MapEditorAdapter implements EditorAdapter {

    @Override
    public void runEditor(String filePath) throws IOException, JDOMException {
        if (filePath == null)
            new Controller();
        else
            new Controller(filePath);
    }
}
