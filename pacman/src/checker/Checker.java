package checker;

import ch.aplu.jgamegrid.Location;
import org.jdom.JDOMException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract Checker class, containing helper methods shared by the concrete
 * checkers (e.g. error-logging and error string builders).
 */
public abstract class Checker {

    private static final String errorLogPath = "pacman/errorLog/errorLogs.txt";
    private ArrayList<String> errors = new ArrayList<>();

    /**
     Inspects and logs errors to the error log file.
     If there are no errors, flushes the error log.
     Otherwise, flushes then logs the errors.
     @return true if no errors are found, false otherwise.
     */
    public boolean inspectAndLogErrors() {
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
     * Concatenates the given list of strings, seperated by semicolons.
     * @param lst: a list of strings.
     * @return the concatenated string.
     */
    protected String semicolonStringBuilder(ArrayList<String> lst){
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
     * Concatenates the given list of Locations, seperated by semicolons.
     * @param locList: a list of locations
     * @return the concatenated string
     */
    protected String semicolonLocationStringBuilder(ArrayList<Location> locList){
        ArrayList<String> locStrLst = new ArrayList<>();
        for (Location loc:locList) {
            // one offset make the initial location (1, 1) instead of (0, 0),
            // as per the spec
            locStrLst.add("(" + (loc.x + 1) + "," + (loc.y + 1) + ")");
        }
        return semicolonStringBuilder(locStrLst);
    }

    /**
     * Checks the validity of the file(s) at the given path
     * @param dir: path to the file(s)
     * @return true if the checks were passes, false otherwise.
     * @throws IOException
     * @throws JDOMException
     */
    public abstract boolean check(String dir) throws IOException, JDOMException;

    /**
     * Adds an error message to the errors list.
     * @param errorMessage: error message to be added
     */
    protected void addError(String errorMessage) {
        if (errorMessage != null)
            errors.add(errorMessage);
    }

    /**
     * Adds all error messages from the given list to the errors list.
     * @param errorMessages: an ArrayList of error messages to be added
     */
    protected void addErrors(ArrayList<String> errorMessages) {
        errors.addAll(errorMessages);
    }

    /**
     * Gets the errors list.
     * @return the ArrayList of error messages.
     */
    public ArrayList<String> getErrors() {
        return errors;
    }
}
