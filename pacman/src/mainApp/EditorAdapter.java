package mainApp;

import org.jdom.JDOMException;

import java.io.IOException;

/**
 * Interface to be implemented by
 */
public interface EditorAdapter {
    /**
     * Runs the editor with the given file as the current view.
     * @param filePath: path to a file, a directory,
     *                  or null for no current view.
     * @throws IOException
     * @throws JDOMException
     */
    void runEditor(String filePath) throws IOException, JDOMException;

}
