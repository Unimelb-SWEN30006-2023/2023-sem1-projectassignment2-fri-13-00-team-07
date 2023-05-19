package checker;

import checker.gameChecks.CheckMapAndMapSequence;
import checker.gameChecks.GameCheck;

import java.util.ArrayList;
import java.util.Collections;

/**
 * GameChecker
 */
public class GameChecker extends Checker {
    private static GameChecker instance = null;
    private final GameCheck checkMapAndMapSequence;
    private ArrayList<String> validFiles = new ArrayList<>();

    public GameChecker() {
        checkMapAndMapSequence = new CheckMapAndMapSequence();
    }

    public static GameChecker getInstance() {
        if (instance == null) {
            instance = new GameChecker();
        }
        return instance;
    }

    public boolean checkGame(String mapFolderDir) {
        validFiles = new ArrayList<>(); //prevent overlap
        ArrayList<String> errors = new ArrayList<>();
        // check
        checkMapAndMapSequence.check(mapFolderDir, errors, validFiles);
        Collections.sort(validFiles);

        // report errors
        return inspectAndLogErrors(errors);
    }

    public ArrayList<String> getValidMapFiles() {
        return validFiles;
    }
}
