package checker;

import ch.aplu.jgamegrid.Location;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Checker
 */
public abstract class Checker {
    private static final String errorLogPath = "pacman/errorLog/errorLogs.txt";
    protected static ErrorMessagesBody errorMessagesBody = ErrorMessagesBody.getInstance();

    protected String semicolonStringBuilder(ArrayList<String> lst){
        // !!! grid start from (1, 1), not (0, 0) change this later
        String str = "";
        for(int i = 0; i < lst.size(); i++){
            str += lst.get(i);
            if(i != lst.size() - 1){
                str += ";";
            }
        }
        return str;
    }

    protected String semicolonLocationStringBuilder(ArrayList<Location> locList){
        ArrayList<String> locStrLst = new ArrayList<>();
        for(Location loc:locList){
            locStrLst.add("(" + loc.x + "," + loc.y + ")");
        }
        return semicolonStringBuilder(locStrLst);
    }

    protected void logErrors(ArrayList<String> errors){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorLogPath, false))) {
            for (String line : errors) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Write to error log successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while appending the errors to the errorLogs: " + e.getMessage());
        }
    }
}
