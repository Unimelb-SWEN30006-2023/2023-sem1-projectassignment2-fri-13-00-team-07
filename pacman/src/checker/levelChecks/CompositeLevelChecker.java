package checker.levelChecks;

import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * A composite checker for overall level checking.
 */
public class CompositeLevelChecker extends LevelChecker {
    private final ArrayList<LevelChecker> individualLevelCheckers = new ArrayList<>();
    // this leaf checker will be treated separately
    private GoldPillAccessibilityChecker goldPillAccessibilityChecker;

    /**
     * Constructs a CompositeLevelChecker.
     */
    public CompositeLevelChecker() {
        individualLevelCheckers.add(new PacStartChecker());
        individualLevelCheckers.add(new PortalPairChecker());
        individualLevelCheckers.add(new NumGoldPillChecker());
        goldPillAccessibilityChecker = new GoldPillAccessibilityChecker();
    }

    /**
     * Checks if a level is valid based on a customizable sequence of maps
     * @return true if all tests are passed;
     *         otherwise, returns false and logs errors.
     */
    @Override
    public boolean check(EditorMap map) {
        boolean pacStartFlag = true;
        boolean portalPairFlag = true;
        for (LevelChecker check : individualLevelCheckers) {
            boolean flag = check.check(map);
            if (check instanceof PacStartChecker)
                pacStartFlag = flag;
            else if (check instanceof PortalPairChecker)
                portalPairFlag = flag;
        }

        // only check if gold/pill is accessible if
        // 1. The PacActor's start location is valid
        // 2. The portals are valid (as per the spec)
        if (pacStartFlag && portalPairFlag) {
            goldPillAccessibilityChecker.check(map);
        }

        // gather the errors
        for (LevelChecker check : individualLevelCheckers) {
            addErrors(check.getErrors());
        }

        addErrors(goldPillAccessibilityChecker.getErrors());
        // report errors
        return inspectAndLogErrors();
    }
}
