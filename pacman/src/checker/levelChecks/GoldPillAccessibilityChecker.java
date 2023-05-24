package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.ErrorMessageBody;
import game.CharacterType;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * Checks if all gold and pills are accessible in a level.
 * Should only be run after PacStartChecker and NumGoldPillChecker have checked.
 */
public class GoldPillAccessibilityChecker extends LevelChecker {
    private boolean flag = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(EditorMap map) {
        ArrayList<Location> golds = new ArrayList<>();
        ArrayList<Location> pills = new ArrayList<>();
        Location pacLocation = null;
        // extract all golds, pills, and pac locations
        for (int i = 0; i < map.getVerticalCellsCount(); i++) {
            for (int j = 0; j < map.getHorizontalCellsCount(); j++) {
                Location loc = new Location(j, i);
                if (map.getTypeAt(loc) == CellType.GOLD) {
                    golds.add(loc);
                } else if (map.getTypeAt(loc) == CellType.PILL) {
                    pills.add(loc);
                } else if (map.getTypeAt(loc) == CharacterType.PACMAN) {
                    // We would use PacStartChecker in advance, so there shouldn't be multiple start Location
                    // But for safety, still check this here (since we are already in a loop)
                    if (pacLocation != null) { // second occurrence
                        return false;
                    }
                    pacLocation = loc;
                }
            }
        }

        if (pacLocation == null) { // no valid PacActor starting point
            return false;
        }

        checkItemErrors(golds, ErrorMessageBody.LEVEL_D_GOLD_NOT_ACC, map, pacLocation);
        checkItemErrors(pills, ErrorMessageBody.LEVEL_D_PILL_NOT_ACC, map, pacLocation);

        return flag;
    }

    /**
     * Checks the validity (accessibility) of the item locations,
     * and adds the relevant error message if required.
     * @param originalItems: an ArrayList of the original item locations
     * @param errorMessageBody: error message to be added
     * @param map: map being checked
     * @param pacLocation: initial location of the PacPlayer
     */
    private void checkItemErrors(ArrayList<Location> originalItems,
                                 String errorMessageBody, EditorMap map,
                                 Location pacLocation) {
        ArrayList<Location> errorItems = new ArrayList<>();
        // pick out the actual error items
        for (Location loc : originalItems) {
            // check item's accessibility
            if (!map.canReach(pacLocation, loc)) {
                errorItems.add(loc);
            }
        }

        if (errorItems.size() > 0) { // failed?
            flag = false;
            addError(formatLogHeader(map.getFileName()) + errorMessageBody + semicolonLocationStringBuilder(errorItems));
        }
    }
}
