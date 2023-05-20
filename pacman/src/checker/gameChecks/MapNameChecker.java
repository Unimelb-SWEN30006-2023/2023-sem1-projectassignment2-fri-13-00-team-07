package checker.gameChecks;

import checker.Checker;
import checker.ErrorMessageBody;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * check if map sequence is defined correctly within the specified folder
 * update errors if any. update validFileNames is there is a valid map sequence
 */
public class MapNameChecker extends Checker {
    private ArrayList<String> validFileNames = new ArrayList<>();
    private ArrayList<String> filenameStore = new ArrayList<>();
    private String mapFolderDir;


    @Override
    public boolean check(String mapFolderDir) {
        this.mapFolderDir = mapFolderDir;
        Path dir = Paths.get(mapFolderDir);
        // Check if the given path is a directory
        if (!checkValidDirectory(dir))
            return false;

        if (!filterFilenames(dir))
            return false;

        /* check 1. there has to be at least one map with valid name in directory */
        if (!checkAtLeastOneValidMapName())
            return false;

        boolean flag = true;
        /* check 2. map sequence is correct */
        //extract all sequence prefix
        HashMap<Integer, ArrayList<Integer>> numericPrefixes = extractNumericPrefixes();
        // build valid filenames
        return extractValidFileNames(numericPrefixes);
    }

    private boolean checkValidDirectory(Path dir) {
        if (!Files.isDirectory(dir)) {
            addError(ErrorMessageBody.GAME_INVALID_DIR);
            return false;
        }
        return true;
    }

    private boolean filterFilenames(Path dir) {
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
            addError(ErrorMessageBody.GAME_FAIL_IO);
            return false;
        }
        return true;
    }

    private boolean checkAtLeastOneValidMapName() {
        if (filenameStore.size() == 0) {
            addError(mapFolderDir + ErrorMessageBody.GAME_NO_MAPS_FOUND);
            return false;
        }
        return true;
    }

    private HashMap<Integer, ArrayList<Integer>> extractNumericPrefixes() {
        HashMap<Integer, ArrayList<Integer>> numericPrefixes = new HashMap<>();
        for (int i = 0; i < filenameStore.size(); i++) {
            // get prefix
            int k = 0;
            while (Character.isDigit(filenameStore.get(i).charAt(k))) {
                k++;
            }
            int digit = Integer.parseInt(filenameStore.get(i).substring(0, k));
            // discard 0
            if (digit == 0) {
                continue;
            }
            if (numericPrefixes.containsKey(digit)) {
                numericPrefixes.get(digit).add(i);
            } else {
                ArrayList<Integer> newList = new ArrayList<>();
                newList.add(i);
                numericPrefixes.put(digit, newList);
            }
        }
        return numericPrefixes;
    }

    private boolean checkNumericPrefixes(HashMap<Integer, ArrayList<Integer>> numericPrefixes) {
        boolean flag = true;
        for (int digit : numericPrefixes.keySet()) {
            if (numericPrefixes.get(digit).size() > 1) {
                String errorStr = mapFolderDir + ErrorMessageBody.GAME_MULTI_MAPS_SAME_LEVEL;
                ArrayList<String> filenameList = new ArrayList<>();
                for (int i : numericPrefixes.get(digit)) {
                    filenameList.add(filenameStore.get(i));
                }
                addError(errorStr + semicolonStringBuilder(filenameList));
                flag = false;
            }
        }
        return flag;
    }

    private boolean extractValidFileNames(HashMap<Integer, ArrayList<Integer>> numericPrefixes) {
        if (!checkNumericPrefixes(numericPrefixes))
            return false;

        for (int digit : numericPrefixes.keySet()) {
            if (numericPrefixes.get(digit).size() == 1) {
                validFileNames.add(filenameStore.get(numericPrefixes.get(digit).get(0)));
            }
        }
        return true;
    }

    public ArrayList<String> getValidFileNames() {
        return validFileNames;
    }
}
