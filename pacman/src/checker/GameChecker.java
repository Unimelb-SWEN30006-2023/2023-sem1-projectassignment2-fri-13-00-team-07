package checker;

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
    private static final String levelDir = "pacman/src/data/wrongSequenceMapFolder_GameCheckerTest";

    public GameChecker() {;}

    public static GameChecker getInstance() {
        if (instance == null) {
            instance = new GameChecker();
        }
        return instance;
    }

    /**check
     * 1. at least one map in the folder
     * 2. map sequence is well-defined (don't have to be continous)
     */
    public void verifyGame(ArrayList<String> errors) {
//        System.out.println("here");
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println("here");
        ArrayList<String> filenameStore = new ArrayList<>();
        Path dir = Paths.get(levelDir);
        // Check if the given path is a directory
        if (!Files.isDirectory(dir)) {
            throw new RuntimeException("verify game - directory invalid"); //delete later
            //return;
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
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("verify game - IO exception"); //delete later
        }
        /* check 1. there has to be at least one map with valid name in directory */
        if(filenameStore.size() == 0){
            errors.add(levelDir + errorMessagesBody.GAME_NO_MAPS_FOUND);
            return;
        }
        /* check 2. map sequence is correct */
        //extract all sequence prefix
        HashMap<Integer, ArrayList<Integer>> hm = new HashMap<>();
        for(int i = 0; i < filenameStore.size(); i++){
            // get prefix
            int k = 0;
            while(Character.isDigit(filenameStore.get(i).charAt(k))){
                k++;
            }
            int digit = Integer.parseInt(filenameStore.get(i).substring(0, k));
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
                String errorStr = levelDir + errorMessagesBody.GAME_MULTI_MAPS_SAME_LEVEL;
                ArrayList<String> filenameLst = new ArrayList<>();
                for(int i:hm.get(digit)){
                    filenameLst.add(filenameStore.get(i));
                }
                errors.add(errorStr + semicolonStringBuilder(filenameLst));
            }
        }
    }

    public boolean checkGame() {
        ArrayList<String> errors = new ArrayList<>();
        verifyGame(errors);
        // received errors
        if(errors.size() == 0){
            return true;
        }
        else {
            logErrors(errors);
            return false;
        }
    }

}
