package checker.gameChecks;

import checker.Checker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Checker for the validity of a game folder.
 */
public class GameChecker extends Checker {

    /* can add more checks in the future */
    private final MapNameChecker mapNameChecker;

    /**
     * Constructs a GameChecker.
     */
    public GameChecker() {
        mapNameChecker = new MapNameChecker();
    }

    /**
     * Checks if a game is valid and logs errors if required.
     * @param mapFolderDir: the directory of the game folder to be checked.
     * @return true if this game is valid, false otherwise.
     */
    @Override
    public boolean check(String mapFolderDir) {
        mapNameChecker.check(mapFolderDir);

        // report errors
        return mapNameChecker.inspectAndLogErrors();
    }

    /**
     * Gets a list of valid map names after checking.
     * @return an ArrayList of valid map filenames.
     */
    public ArrayList<String> getValidMapFiles() {
        ArrayList<String> validFiles = mapNameChecker.getValidFileNames();
        Collections.sort(validFiles);
        return validFiles;
    }
}
