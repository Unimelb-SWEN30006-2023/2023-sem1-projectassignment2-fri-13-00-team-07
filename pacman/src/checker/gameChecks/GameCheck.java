package checker.gameChecks;

import java.util.ArrayList;

/**
 * All checks applied on the game should implement this interface
 */
public interface GameCheck {
    /**
     * check if a game is valid and update errors and validFileNames correspondingly
     * @param mapFolderDir the dir where all levels are stored
     * @param errors a list of errors to be updated
     * @param validFileNames a list of validFileNames to be updated
     * @return true if this check is passed, otherwise, return false
     */
    boolean check(String mapFolderDir, ArrayList<String> errors, ArrayList<String> validFileNames);
}
