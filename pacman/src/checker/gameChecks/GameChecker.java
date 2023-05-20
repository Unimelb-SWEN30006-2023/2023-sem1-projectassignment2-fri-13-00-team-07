package checker.gameChecks;

import checker.Checker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * a GameChecker, checks if a game is valid based on a customizable sequence of checks.
 */
public class GameChecker extends Checker {

    private final MapNameChecker mapNameChecker;

    public GameChecker() {
        mapNameChecker = new MapNameChecker();
    }

    /**
     * check if a game is valid based on a customizable sequence of checks
     * @param mapFolderDir The directory that contains all maps
     * @return true if this game is valid. otherwise, return false and log errors .
     */
    @Override
    public boolean check(String mapFolderDir) {
        mapNameChecker.check(mapFolderDir);

        // report errors
        return mapNameChecker.inspectAndLogErrors();
    }

    /**
     * get a list of valid map names after checking
     * @return an ArrayList of valid map filenames
     */
    public ArrayList<String> getValidMapFiles() {
        ArrayList<String> validFiles = mapNameChecker.getValidFileNames();
        Collections.sort(validFiles);
        return validFiles;
    }
}
