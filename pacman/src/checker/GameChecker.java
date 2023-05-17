package checker;

import checker.gameChecks.CheckMapAndMapSequence;
import checker.gameChecks.GameCheck;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * GameChecker
 */
public class GameChecker extends Checker {
    private static GameChecker instance = null;
    private final GameCheck checkMapAndMapSequence;

    public GameChecker() {
        checkMapAndMapSequence = new CheckMapAndMapSequence();
    }

    public static GameChecker getInstance() {
        if (instance == null) {
            instance = new GameChecker();
        }
        return instance;
    }

    public boolean checkGame() {
        ArrayList<String> errors = new ArrayList<>();
        // check
        checkMapAndMapSequence.check(errors);
        // report errors
        return inspectAndLogErrors(errors);
    }

}
