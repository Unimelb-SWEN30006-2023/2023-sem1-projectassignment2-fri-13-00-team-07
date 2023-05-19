package checker.gameChecks;

import java.util.ArrayList;

public interface GameCheck {
    boolean check(String mapFolderDir, ArrayList<String> errors, ArrayList<String> validFileNames);
}
