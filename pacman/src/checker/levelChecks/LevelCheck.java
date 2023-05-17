package checker.levelChecks;

import game.Maps.EditorMap;

import java.util.ArrayList;

public interface LevelCheck {
    boolean check(EditorMap map, ArrayList<String> errors);
}
