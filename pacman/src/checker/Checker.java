package checker;

import ch.aplu.jgamegrid.Location;
import org.jdom.JDOMException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * abstract checker class, contains helpers for concrete checkers to use. e.g. inspect and log errors
 */
public abstract class Checker {

    private static final String errorLogPath = "pacman/errorLog/errorLogs.txt";
    private ArrayList<String> errors = new ArrayList<>();

    /**
     Inspects and logs errors to the error log file. If there are no errors, flush error log, otherwise, flush then
     log them
     @param errors An ArrayList of String objects representing the errors to be logged.
     @return true if no errors are found, false otherwise.
     */
    protected boolean inspectAndLogErrors(ArrayList<String> errors) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorLogPath, false))) {
            if (errors.size() == 0) {
                System.out.println("No errors found under this check, flush everything in the errorLogs");
                return true;
            } else {
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

    /**
     * given a list of string, concat them into a single string, seperated by semicolons
     * @param lst a list of string
     * @return a concat string
     */
    protected String semicolonStringBuilder(ArrayList<String> lst){
        // !!! grid start from (1, 1), not (0, 0) change this later
        String str = "";
        for (int i = 0; i < lst.size(); i++) {
            str += lst.get(i);
            if (i != lst.size() - 1) {
                str += "; ";
            }
        }
        return str;
    }

    /**
     * given a list of location, concat them into a single string, seperated by semicolons
     * !!! add one offset for both x and y, required in spec.
     * this will make the initial location (1, 1) instead of (0, 0)
     * @param locList a list of locations
     * @return a location list string, with semicolon seperated
     */
    protected String semicolonLocationStringBuilder(ArrayList<Location> locList){
        ArrayList<String> locStrLst = new ArrayList<>();
        for (Location loc:locList) {
            locStrLst.add("(" + (loc.x + 1) + "," + (loc.y + 1) + ")");
        }
        return semicolonStringBuilder(locStrLst);
    }

    public abstract boolean check(String dir) throws IOException, JDOMException;

    protected void addError(String errorMessage) {
        if (errorMessage != null)
            errors.add(errorMessage);
    }

    protected void addErrors(ArrayList<String> errorMessages) {
        errors.addAll(errorMessages);
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
