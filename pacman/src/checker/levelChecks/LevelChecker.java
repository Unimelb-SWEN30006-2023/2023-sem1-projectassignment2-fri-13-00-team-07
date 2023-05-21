package checker.levelChecks;

import checker.Checker;
import game.Maps.EditorMap;
import org.jdom.JDOMException;

import java.io.IOException;

/**
 * To be implemented by all checks applied on a Level.
 */
public abstract class LevelChecker extends Checker {
    /**
     * {@inheritDoc}
     */
    public boolean check(String mapFile) throws IOException, JDOMException {
        return check(new EditorMap(mapFile));
    }
    /**
     * Ccheck the validity of the EditorMap
     * @param map: an EditorMap (corresponding to a level) to be checked.
     * @return true if the check is passed, false otherwise.
     */
    public abstract boolean check(EditorMap map);
}
