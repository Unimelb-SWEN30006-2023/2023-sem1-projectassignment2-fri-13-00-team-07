package checker;

import ch.aplu.jgamegrid.Location;
import game.utility.GameCallback;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Checker
 */
public abstract class Checker {

    private static final String errorLogPath = "pacman/src/errorLog/errorLogs.txt";
    protected static ErrorMessagesBody errorMessagesBody;

    public Checker() {
        errorMessagesBody = ErrorMessagesBody.getInstance();
    }

    protected static String semicolonStringBuilder(ArrayList<String> lst){
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

    protected static String locationStringBuilder(ArrayList<Location> locList){
        ArrayList<String> locStrLst = new ArrayList<>();
        for(Location loc:locList){
            locStrLst.add("(" + loc.x + "," + loc.y + ")");
        }
        return semicolonStringBuilder(locStrLst);
    }

    protected static void logErrors(ArrayList<String> errors){
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
