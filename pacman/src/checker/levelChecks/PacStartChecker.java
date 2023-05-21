package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.ErrorMessageBody;
import game.CharacterType;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * Check if there is one and only one PacPlayer start location.
 */
public class PacStartChecker extends LevelChecker {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(EditorMap map) {
        boolean flag = true;
        ArrayList<Location> pacStarts = new ArrayList<>();
        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                Location loc = new Location(j, i);
                if (map.isCharacterType(loc) && map.getTypeAt(loc) == CharacterType.PACMAN) {
                    pacStarts.add(loc);
                }
            }
        }

        if (pacStarts.size() == 0) { // no starting point
            flag = false;
            addError(map.getFileName() + ErrorMessageBody.LEVEL_A_NO_START);
        } else if (pacStarts.size() > 1) { // multiple starting points
            addError(map.getFileName() + ErrorMessageBody.LEVEL_A_MULTI_START + semicolonLocationStringBuilder(pacStarts));
            flag = false;
        }
        return flag;
    }
}
