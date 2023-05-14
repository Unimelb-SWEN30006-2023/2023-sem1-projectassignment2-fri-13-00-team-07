package game;

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
public class Checker {
    //Ziming: this only contains the basic logic, I haven't used made the CHECKER interface or parent class, modularize later
    private static Checker instance = null;
    private static final String levelDir = "";

    private Checker(){
        return;
    }

    public static Checker getInstance() {
        if (instance == null) {
            instance = new Checker();
        }
        return instance;
    }

    /**
     * 1. at least one map in the folder
     * 2. map sequence is well-defined (don't have to be continous)
     */
    public static ArrayList<String> checkGame() throws IOException {
        ArrayList<String> errors = new ArrayList<>();
        ArrayList<String> filenameStore = new ArrayList<>();
        Path dir = Paths.get(levelDir);
        // Check if the given path is a directory
        if (!Files.isDirectory(dir)) {
            throw new RuntimeException("GameChecker: dir is not valid");
        }
        // Create a DirectoryStream.Filter to filter only .xml files
        DirectoryStream.Filter<Path> filter = file -> {
            String fileName = file.getFileName().toString();
            return Files.isRegularFile(file) && fileName.endsWith(".xml") && Character.isDigit(fileName.charAt(0));
        };
        // Iterate through the directory and extract .xml file names
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
            for (Path file : stream) {
                filenameStore.add(file.getFileName().toString());
            }
        }
        /* check 1. there has to be at least one map with valid name in directory */
        if(filenameStore.size() == 0){
            errors.add(levelDir + " – no maps found");
            return errors;
        }
        /* check 2. map sequence is correct */
        HashMap<Integer, ArrayList<Integer>> hm = new HashMap<>();
        for(int i = 0; i < filenameStore.size(); i++){
            int digit = Character.getNumericValue(filenameStore.get(i).charAt(0));
            if(hm.containsKey(digit)){
                hm.get(digit).add(i);
            }
            else{
                ArrayList<Integer> newList = new ArrayList<>();
                newList.add(i);
                hm.put(digit, newList);
            }
        }
        //build error messages
        for(int digit:hm.keySet()){
           if(hm.get(digit).size() > 1){
               String errorStr = levelDir + " – multiple maps at same level: ";
               for(int i:hm.get(digit)){
                   errorStr += (filenameStore.get(i) + ".xml; ");
               }
               errors.add(errorStr);
           }
        }
        return errors;
    }
}
