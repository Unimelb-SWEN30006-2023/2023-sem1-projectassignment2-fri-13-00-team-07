package checker.levelChecks;

import checker.levelChecks.*;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * a CompositeLevelChecker. Checks if a level is valid based on a customizable sequence of maps
 */
public class CompositeLevelChecker extends LevelCheck {
    private final ArrayList<LevelCheck> individualLevelChecks = new ArrayList<>();
    // will be treated separately
    private GoldPillAccessibleChecker goldPillAccessibleChecker;


    public CompositeLevelChecker() {
        individualLevelChecks.add(new PacStartChecker());
        individualLevelChecks.add(new PortalPairChecker());
        individualLevelChecks.add(new NumGoldPillChecker());
        goldPillAccessibleChecker = new GoldPillAccessibleChecker();
    }

    /**
     * Checks if a level is valid based on a customizable sequence of maps
     * @return true if all tests are passed. otherwise, return false and log errors
     */
    @Override
    public boolean check(EditorMap map) {
        boolean pacStartFlag = true;
        boolean portalPairFlag = true;
        for (LevelCheck check : individualLevelChecks) {
            boolean flag = check.check(map);
            if (check instanceof PacStartChecker)
                pacStartFlag = flag;
            else if (check instanceof PortalPairChecker)
                portalPairFlag = flag;
        }

        // only check if gold/pill is accessible if
        // 1. The PacActor's start location is valid
        // 2. The portals are valid
        if (pacStartFlag && portalPairFlag) {
            goldPillAccessibleChecker.check(map);
        }

        // gather the errors
        for (LevelCheck check : individualLevelChecks) {
            addErrors(check.getErrors());
        }

        addErrors(goldPillAccessibleChecker.getErrors());
        // report errors
        return inspectAndLogErrors(getErrors());
    }
}