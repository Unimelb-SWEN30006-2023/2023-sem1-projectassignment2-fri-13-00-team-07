package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.CharacterType;
import game.Items.CellType;
import game.Player.OptimalPathFindingStrategy;
import game.Workers.MapReader;
import mapeditor.editor.Controller;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * A map, typically one loaded from a `.xml` file on disk,
 * and is used for the map editor.
 */
public class EditorMap implements PacManMap {

    private final ActorType[][] map;
    private String fileName;


    /**
     * Creates a map using the internal representation of the Map Editor View Controller.
     *
     * @param mazeArray: the 2D array map, using the editor's internal representations (characters).
     */
    public EditorMap(char[][] mazeArray) {
        this.map = new ActorType[mazeArray.length][mazeArray[0].length];

        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                this.map[i][j] = Controller.getCharToActorTypeDict().getOrDefault(mazeArray[i][j], CellType.SPACE);
            }
        }
    }

    /**
     * Creates a map using the internal representation of the Map Editor View Controller.
     *
     * @param mazeArray: the 2D array map, using the editor's internal representations (characters).
     * @param fileName: filename of the map file (source of the 2D array map).
     */
    public EditorMap(char[][] mazeArray, String fileName) {
        this(mazeArray);
        this.fileName = fileName;
    }

    /**
     * Creates a map from a given file path (should be an xml file).
     *
     * @param filePath: The absolute or relative path of the file,
     *                with working directory at the top level (the folder with `pacman`).
     */
    /* Method adapted from the original loadFile() method in mapeditor.editor.Controller */
    public EditorMap(String filePath) throws IOException, JDOMException {
        this.fileName = filePath;

        SAXBuilder builder = new SAXBuilder();
        File file = new File(filePath);
        if (file.canRead() && file.exists()) {
            Document document = builder.build(file);

            Element rootNode = document.getRootElement();

            final List sizeList = rootNode.getChildren("size");
            Element sizeElem = (Element) sizeList.get(0);
            int height = Integer.parseInt(sizeElem.getChildText("height"));
            int width = Integer.parseInt(sizeElem.getChildText("width"));

            this.map = new ActorType[height][width];

            final List rows = rootNode.getChildren("row");
            System.out.println(rows.getClass());
            for (int y = 0; y < rows.size(); y++) {
                Element cellsElem = (Element) rows.get(y);
                final List cells = cellsElem.getChildren("cell");

                for (int x = 0; x < cells.size(); x++) {
                    Element cell = (Element) cells.get(x);
                    String cellValue = cell.getText();

                    this.map[y][x] = Controller.getStrToActorTypeDict().getOrDefault(cellValue, CellType.SPACE);
                }
            }
        } else {
            throw new IOException("Cannot read the file.");
        }
    }


    /**
     * @return The name of the file used to construct the map.
     */
    public String getFileName(){
        return fileName;
    }

    /** {@inheritDoc} */
    @Override
    public ActorType getTypeAt(Location loc) {
        return map[loc.y][loc.x];
    }

    /**
     * Checks whether the location is for an item (i.e. of CellType).
     * @param loc: the location to be checked.
     * @return true if the location is for an item, false otherwise.
     */
    public boolean isCellType(Location loc) {
        return map[loc.y][loc.x] instanceof CellType;
    }

    /** {@inheritDoc} */
    @Override
    public HashMap<Integer, ActorType> readMyItemLocations(MapReader reader) {
        return reader.getItemLocations(this);
    }

    /** {@inheritDoc} */
    @Override
    public HashMap<Integer, ActorType> readMyCharacterLocations(MapReader reader) {
        return reader.getCharacterLocations(this);
    }

    /**
     * Checks whether the location is for a game character (i.e. of CharacterType).
     * @param loc: the location to be checked.
     * @return true if the location is for a character (i.e. a MovingActor), false otherwise.
     */
    public boolean isCharacterType(Location loc){
        return map[loc.y][loc.x] instanceof CharacterType;
    }

    /** {@inheritDoc} */
    @Override
    public int getVerticalCellsCount() {
        return map.length;
    }

    /** {@inheritDoc} */
    @Override
    public int getHorizontalCellsCount() {
        return map[0].length;
    }

    /**
     * Checks if the destination (`to`) is reachable from the starting location (`from`),
     * considering only walls as obstacles.
     * @param from: start location
     * @param to: destination location
     * @return true if accessible, false if not.
     */
    public boolean canReach(Location from, Location to) {
        return (new OptimalPathFindingStrategy().findPath(from, to, this) != null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWallAt(Location location) {
        return map[location.y][location.x] == CellType.WALL;
    }

}
