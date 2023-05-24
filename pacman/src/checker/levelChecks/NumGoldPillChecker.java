package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.ErrorMessageBody;
import game.Items.CellType;
import game.Maps.EditorMap;

/**
 * Check if the total number of golds and pills is at least 2.
 */
public class NumGoldPillChecker extends LevelChecker {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(EditorMap map) {
        int counter = 0;
        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                Location loc = new Location(j, i);
                if (map.getTypeAt(loc) == CellType.GOLD || map.getTypeAt(loc) == CellType.PILL) {
                    counter ++;
                }
            }
        }

        if (counter < 2) {
            addError(formatLogHeader(map.getFileName()) + ErrorMessageBody.LEVEL_C_LESS_TWO_GOLD_PILL);
            return false;
        }
        return true;
    }
}
