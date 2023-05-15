package game;

import game.utility.GameCallback;

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
    static GameCallback gameCallBack;
    protected static void logErrors(ArrayList<String> errors){
        for(String error: errors){
            gameCallBack.writeString(error);
        }
    }
}
