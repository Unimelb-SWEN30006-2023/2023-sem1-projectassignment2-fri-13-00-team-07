package checker;

import ch.aplu.jgamegrid.Location;
import checker.levelChecks.*;
import game.CharacterType;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

    public boolean checkLevel(EditorMap map){
        ArrayList<String> errors = new ArrayList<>();
        boolean pacStartFlag = checkPacStart.check(map, errors);
        boolean portalPairFlag = checkPortalPair.check(map, errors);
        boolean numGoldPillFlag = checkNumGoldPill.check(map, errors);
        // only check if gold/pill accessible if
        // 1. valid pac start location
        // 2. valid portals
        if(pacStartFlag && portalPairFlag){
            checkGoldPillAccessible.check(map, errors);
        }
        // report errors
        return inspectAndLogErrors(errors);
    }


}
