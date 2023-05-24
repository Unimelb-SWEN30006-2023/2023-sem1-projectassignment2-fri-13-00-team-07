package checker.gameChecks;

import checker.Checker;
import checker.ErrorMessageBody;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Checker for the validity of map name sequence in a game folder.
 */
public class MapNameChecker extends Checker {
    private ArrayList<String> validFileNames = new ArrayList<>();
    private ArrayList<String> filenameStore = new ArrayList<>();
    /* here we keep the full directory name for identification purpose */
    private String mapFolderDir;


    /**
     * {@inheritDoc}
     */
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

        /* check 2. map sequence is correct */
        //extract all sequence prefix
        HashMap<Integer, ArrayList<Integer>> numericPrefixes = extractNumericPrefixes();
        // build valid filenames
        return extractValidFileNames(numericPrefixes);
    }

    /**
     * Checks if the given file is a valid XML file
     * @param path: the path of the file to be checked
     * @return true if it's a valid XML file, false otherwise.
     * @throws JDOMException
     * @throws IOException
     */
    private boolean isValidXML(Path path) {
        SAXBuilder builder = new SAXBuilder();
        try {
            builder.build(path.toFile());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the directory is valid.
     * @param dir: directory (path) to be checked.
     * @return true if the directory is valid, false otherwise.
     */
    private boolean checkValidDirectory(Path dir) {
        if (!Files.isDirectory(dir)) {
            addError(ErrorMessageBody.GAME_INVALID_DIR);
            return false;
        }
        return true;
    }

    /**
     * Filters for the valid filenames in the given path.
     * @param dir: directory (path) to be checked.
     * @return
     */
    private boolean filterFilenames(Path dir) {
        // Create a DirectoryStream.Filter to filter only valid map files
        DirectoryStream.Filter<Path> filter = file -> {
            String fileName = file.getFileName().toString();
            return Files.isRegularFile(file) && isValidXML(file) && Character.isDigit(fileName.charAt(0));
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

    /**
     * Checks if there is at least one valid map name in the filenames stored.
     * @return true if there exists a valid map name, false otherwise.
     */
    private boolean checkAtLeastOneValidMapName() {
        if (filenameStore.size() == 0) {
            addError(formatLogHeader(mapFolderDir) + ErrorMessageBody.GAME_NO_MAPS_FOUND);
            return false;
        }
        return true;
    }

    /**
     * Extracts the numeric prefixes from the filenames stored.
     * @return a HashMap mapping the map sequence number (integer) to
     *         the list of index of the files with this sequence number
     *         in `filenameStore`.
     */
    private HashMap<Integer, ArrayList<Integer>> extractNumericPrefixes() {
        HashMap<Integer, ArrayList<Integer>> numericPrefixes = new HashMap<>();
        for (int i = 0; i < filenameStore.size(); i++) {
            // get prefix
            int k = 0;
            // extract the numeric component in the prefix
            while (Character.isDigit(filenameStore.get(i).charAt(k))) {
                k++;
            }
            int digit = Integer.parseInt(filenameStore.get(i).substring(0, k));
            if (digit == 0) { // discard 0
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

    /**
     * Checks the validity of the given HashMap of numeric prefixes.
     * @param numericPrefixes: a HashMap mapping the map sequence number (integer) to
     *                         the list of index of the files with this sequence number
     *                         in `filenameStore`.
     * @return true if this mapping is valid, false otherwise.
     */
    private boolean checkNumericPrefixes(HashMap<Integer, ArrayList<Integer>> numericPrefixes) {
        boolean flag = true;
        for (int sequenceNumber : numericPrefixes.keySet()) {
            if (numericPrefixes.get(sequenceNumber).size() > 1) {
                // more than one map file for this sequence number
                String errorStr = mapFolderDir + ErrorMessageBody.GAME_MULTI_MAPS_SAME_LEVEL;
                ArrayList<String> filenameList = new ArrayList<>();
                for (int i : numericPrefixes.get(sequenceNumber)) {
                    filenameList.add(filenameStore.get(i));
                }
                addError(formatLogHeader(errorStr) + semicolonStringBuilder(filenameList));
                flag = false;
            }
        }
        return flag;
    }

    /**
     * First checks the validity of the given HashMap of numeric prefixes.
     * If valid, then extracts the valid filenames from the HashMap.
     * @param numericPrefixes: a HashMap mapping the map sequence number (integer) to
     *                         the list of index of the files with this sequence number
     *                         in `filenameStore`.
     * @return true if the numeric prefixes HashMap is valid, false otherwise.
     */
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

    /**
     * Gets the valid filenames.
     * @return an ArrayList of Strings representing the valid filenames.
     */
    public ArrayList<String> getValidFileNames() {
        return validFileNames;
    }

    /**
     * Formats the 'header' filename of an error message for the log.
     * @param folderName: the original name of the game folder.
     * @return the formatted header, as a String.
     */
    private String formatLogHeader(String folderName) {
        return "Game " + folderName;
    }
}
