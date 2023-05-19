package checker.gameChecks;

import checker.Check;
import checker.ErrorMessagesBody;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckMapAndMapSequence extends Check implements GameCheck{

    @Override
    public boolean check(String mapFolderDir, ArrayList<String> errors) {
        ArrayList<String> filenameStore = new ArrayList<>();
        Path dir = Paths.get(mapFolderDir);
        // Check if the given path is a directory
        if (!Files.isDirectory(dir)) {
            errors.add(ErrorMessagesBody.GAME_INVALID_DIR);
            return false;
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
            errors.add(ErrorMessagesBody.GAME_FAIL_IO);
            return false;
        }

        /* check 1. there has to be at least one map with valid name in directory */
        if (filenameStore.size() == 0) {
            errors.add(mapFolderDir + ErrorMessagesBody.GAME_NO_MAPS_FOUND);
            return false;
        }
        boolean flag = true;
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
        for (int digit:hm.keySet()) {
            if (hm.get(digit).size() > 1) {
                String errorStr = mapFolderDir + ErrorMessagesBody.GAME_MULTI_MAPS_SAME_LEVEL;
                ArrayList<String> filenameLst = new ArrayList<>();
                for (int i:hm.get(digit)) {
                    filenameLst.add(filenameStore.get(i));
                }
                errors.add(errorStr + semicolonStringBuilder(filenameLst));
                flag = false;
            }
        }
        return flag;
    }
}
