package checker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Checker
 */
public abstract class Checker {
    private static final String errorLogPath = "pacman/errorLog/errorLogs.txt";
    private CheckerType type;

    public Checker(CheckerType type) {
        this.type = type;
    }

    protected boolean inspectAndLogErrors(ArrayList<String> errors){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorLogPath, false))) {
            if(errors.size() == 0){
                System.out.println("No errors found under this check, flush everything in the errorLogs");
                return true;
            }
            else {
                for (String line : errors) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Some checks failed, write to errorLogs");
                return false;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while appending the errors to the errorLogs: " + e.getMessage());
        }
        return false;
    }
}
