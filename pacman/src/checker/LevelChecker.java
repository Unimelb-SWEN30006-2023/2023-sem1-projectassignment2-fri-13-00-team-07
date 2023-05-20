package checker;

import checker.levelChecks.*;
import game.Maps.EditorMap;

import java.util.ArrayList;

public class LevelChecker extends Checker {
    private final LevelCheck checkPacStart;
    private final LevelCheck checkPortalPair;
    private final LevelCheck checkNumGoldPill;
    private final LevelCheck checkGoldPillAccessible;

    public LevelChecker() {
        super(CheckerType.LEVEL_CHECKER);
        checkPacStart = new CheckPacStart();
        checkPortalPair = new CheckPortalPair();
        checkNumGoldPill = new CheckNumGoldPill();
        checkGoldPillAccessible = new CheckGoldPillAccessible();
    }

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
