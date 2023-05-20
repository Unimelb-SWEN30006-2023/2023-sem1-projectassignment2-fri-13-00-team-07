package checker;

import checker.levelChecks.*;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * a LevelChecker. Checks if a level is valid based on a customizable sequence of maps
 */
public class LevelChecker extends Checker {
    private static LevelChecker instance = null;
    private final LevelCheck checkPacStart;
    private final LevelCheck checkPortalPair;
    private final LevelCheck checkNumGoldPill;
    private final LevelCheck checkGoldPillAccessible;

    public LevelChecker() {
        checkPacStart = new CheckPacStart();
        checkPortalPair = new CheckPortalPair();
        checkNumGoldPill = new CheckNumGoldPill();
        checkGoldPillAccessible = new CheckGoldPillAccessible();
    }

    public static LevelChecker getInstance() {
        if (instance == null) {
            instance = new LevelChecker();
        }
        return instance;
    }

    /**
     * Checks if a level is valid based on a customizable sequence of maps
     * @param map the level to be checked
     * @return true if all tests are passed. otherwise, return false and log errors
     */
    public boolean checkLevel(EditorMap map){
        ArrayList<String> errors = new ArrayList<>();
        boolean pacStartFlag = checkPacStart.check(map, errors);
        boolean portalPairFlag = checkPortalPair.check(map, errors);
        boolean numGoldPillFlag = checkNumGoldPill.check(map, errors);
        // only check if gold/pill accessible if
        // 1. valid pac start location
        // 2. valid portals
        if (pacStartFlag && portalPairFlag) {
            checkGoldPillAccessible.check(map, errors);
        }
        // report errors
        return inspectAndLogErrors(errors);
    }


}
