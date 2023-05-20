package checker.gameChecks;

import checker.Checker;
import checker.gameChecks.MapAndMapSequenceChecker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * a GameChecker, checks if a game is valid based on a customizable sequence of checks.
 */
public class GameChecker extends Checker {

    private final MapAndMapSequenceChecker mapAndMapSequenceChecker;

    public GameChecker() {
        mapAndMapSequenceChecker = new MapAndMapSequenceChecker();
    }

    /**
     * check if a game is valid based on a customizable sequence of checks
     * @param mapFolderDir The directory that contains all maps
     * @return true if this game is valid. otherwise, return false and log errors .
     */
    @Override
    public boolean check(String mapFolderDir) {
        mapAndMapSequenceChecker.check(mapFolderDir);

        // report errors
        return inspectAndLogErrors(mapAndMapSequenceChecker.getErrors());
    }

    /**
     * get a list of valid map names after checking
     * @return an ArrayList of valid map filenames
     */
    public ArrayList<String> getValidMapFiles() {
        ArrayList<String> validFiles = mapAndMapSequenceChecker.getValidFileNames();
        Collections.sort(validFiles);
        return validFiles;
    }
}
