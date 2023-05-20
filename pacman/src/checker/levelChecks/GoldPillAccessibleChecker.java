package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.ErrorMessageBody;
import game.CharacterType;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * Check if all gold and pills are accessible in a level
 * Only make sense to run this check if other tests are passed
 */
public class GoldPillAccessibleChecker extends LevelCheck {
    private boolean flag = true;

    @Override
    public boolean check(EditorMap map) {
        ArrayList<Location> golds = new ArrayList<>();
        ArrayList<Location> pills = new ArrayList<>();
        ArrayList<Location> errorGolds = new ArrayList<>();
        ArrayList<Location> errorPills = new ArrayList<>();
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
                    // !!! in theory, if run verifyPacStartPoint in advance, there shouldn't be multiple start Location
                    // but for safety concerns, check if there are multiple start here
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

        // build gold error string
        buildErrors(golds, errorGolds, ErrorMessageBody.LEVEL_D_GOLD_NOT_ACC, map, pacLocation);

        // build pill error String
        buildErrors(pills, errorPills, ErrorMessageBody.LEVEL_D_PILL_NOT_ACC, map, pacLocation);

        return flag;
    }

    private void buildErrors(ArrayList<Location> originalItems, ArrayList<Location> errorItems, String errorMessageBody, EditorMap map, Location pacLocation) {
        for (Location loc : originalItems) {
            if (!map.canReach(pacLocation, loc)) {
                errorItems.add(loc);
            }
        }

        if (errorItems.size() > 0) {
            flag = false;
            addError(map.getFileName() + errorMessageBody + semicolonLocationStringBuilder(errorItems));
        }
    }
}
