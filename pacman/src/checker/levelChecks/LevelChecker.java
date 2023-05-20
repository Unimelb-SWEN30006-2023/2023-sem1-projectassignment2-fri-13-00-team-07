package checker.levelChecks;

import checker.Checker;
import game.Maps.EditorMap;
import org.jdom.JDOMException;

import java.io.IOException;

/**
 * All checks applied on a level should implement this interface
 */
public abstract class LevelChecker extends Checker {

    public boolean check(String mapFile) throws IOException, JDOMException {
        return check(new EditorMap(mapFile));
    }
    /**
     * given an EditorMap and an ArrayList of errors, apply check on map and update errors
     * @param map the level to be checked
     * @return true if a check is passed, false otherwise
     */
    public abstract boolean check(EditorMap map);
}
