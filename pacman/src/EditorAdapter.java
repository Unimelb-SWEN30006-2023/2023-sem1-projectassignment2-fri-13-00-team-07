import org.jdom.JDOMException;

import java.io.IOException;

public interface EditorAdapter {
    void runEditor(String filename) throws IOException, JDOMException; // specified current map, or null for no current map
    boolean applyLevelCheck();

}
