package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.ErrorMessageBody;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Check that all portal pairs are valid.
 */
public class PortalPairChecker extends LevelChecker {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check(EditorMap map) {
        boolean flag = true;
        HashMap<CellType, ArrayList<Location>> portalLocations = map.getPortalLocations();

        for (CellType type : portalLocations.keySet()) {
            ArrayList<Location> locationList = portalLocations.get(type);
            // should have exactly two tiles for each portal
            if (locationList.size() != 2) {
                flag = false;
                addError(formatLogHeader(map.getFileName()) + " - " + type.getName() + ErrorMessageBody.LEVEL_B_NOT_TWO_PORTAL + semicolonLocationStringBuilder(locationList));
            }
        }
        return flag;
    }
}
