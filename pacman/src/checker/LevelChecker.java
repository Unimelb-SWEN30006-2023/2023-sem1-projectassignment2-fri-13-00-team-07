package checker;

import checker.levelChecks.*;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * a LevelChecker. Checks if a level is valid based on a customizable sequence of maps
 */
public class LevelChecker extends Checker {
    private final LevelCheck checkPacStart;
    private final LevelCheck checkPortalPair;
    private final LevelCheck checkNumGoldPill;
    private final LevelCheck checkGoldPillAccessible;

    private static LevelChecker instance;

    public LevelChecker() {
        super(CheckerType.LEVEL_CHECKER);
        checkPacStart = new CheckPacStart();
        checkPortalPair = new CheckPortalPair();
        checkNumGoldPill = new CheckNumGoldPill();
        checkGoldPillAccessible = new CheckGoldPillAccessible();
    }

    /**
     * Checks if a level is valid based on a customizable sequence of maps
     * @param map the level to be checked
     * @return true if all tests are passed. otherwise, return false and log errors
     */
    public boolean checkLevel(EditorMap map) {
        ArrayList<String> errors = new ArrayList<>();
        boolean pacStartFlag = checkPacStart.check(map, errors);
        boolean portalPairFlag = checkPortalPair.check(map, errors);
        boolean numGoldPillFlag = checkNumGoldPill.check(map, errors);
        // only check if gold/pill is accessible if
        // 1. The PacActor's start location is valid
        // 2. The portals are valid
        if (pacStartFlag && portalPairFlag) {
            checkGoldPillAccessible.check(map, errors);
        }
        // report errors
        return inspectAndLogErrors(errors);
    }


}
