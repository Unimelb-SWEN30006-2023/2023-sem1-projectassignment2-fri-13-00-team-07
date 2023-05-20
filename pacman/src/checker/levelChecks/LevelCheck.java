package checker.levelChecks;

import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * All checks applied on a level should implement this interface
 */
public interface LevelCheck {
    /**
     * given an EditorMap and an ArrayList of errors, apply check on map and update errors
     * @param map the level to be checked
     * @param errors errors list to be updated
     * @return true if a check is passed, false otherwise
     */
    boolean check(EditorMap map, ArrayList<String> errors);
}
