package checker;

import checker.gameChecks.CheckMapAndMapSequence;
import checker.gameChecks.GameCheck;

import java.util.ArrayList;
import java.util.Collections;

/**
 * a GameChecker, checks if a game is valid based on a customizable sequence of checks.
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

    /**
     * check if a game is valid based on a customizable sequence of checks
     * @param mapFolderDir The directory that contains all maps
     * @return true if this game is valid. otherwise, return false and log errors .
     */
    public boolean checkGame(String mapFolderDir) {
        validFiles = new ArrayList<>(); //prevent overlap
        ArrayList<String> errors = new ArrayList<>();
        // check
        checkMapAndMapSequence.check(mapFolderDir, errors, validFiles);
        Collections.sort(validFiles);

        // report errors
        return inspectAndLogErrors(errors);
    }

    /**
     * get a list of valid map names after checking
     * @return an ArrayList of valid map filenames
     */
    public ArrayList<String> getValidMapFiles() {
        return validFiles;
    }
}
