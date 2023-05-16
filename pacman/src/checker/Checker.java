package checker;

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

    private static final String errorLogPath = "/errorLogs.txt";
    protected static ErrorMessagesBody errorMessagesBody;

    public Checker() {
        errorMessagesBody = ErrorMessagesBody.getInstance();
    }

    protected static void logErrors(ArrayList<String> errors){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorLogPath, true))) {
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
