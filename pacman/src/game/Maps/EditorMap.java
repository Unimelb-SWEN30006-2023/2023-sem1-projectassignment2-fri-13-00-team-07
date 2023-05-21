package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.CharacterType;
import game.Items.CellType;
import game.Player.OptimalPathFindingStrategy;
import game.Workers.MapReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * A map, typically one loaded from a `.xml` file on disk.
 */
public class EditorMap implements PacManMap {

    private final ActorType[][] map;
    private String fileName;

    private final static HashMap<Character, ActorType> editorRepresentationDictionary = new HashMap<>() {{
        put('a', CellType.SPACE);
        put('b', CellType.WALL);
        put('c', CellType.PILL);
        put('d', CellType.GOLD);
        put('e', CellType.ICE);
        put('f', CharacterType.PACMAN);
        put('g', CharacterType.M_TROLL);
        put('h', CharacterType.M_TX5);
        put('i', CellType.PORTAL_WHITE);
        put('j', CellType.PORTAL_YELLOW);
        put('k', CellType.PORTAL_DARK_GOLD);
        put('l', CellType.PORTAL_DARK_GRAY);
    }};

    private final static HashMap<String, ActorType> xmlRepresentationDictionary = new HashMap<>() {{
        put("PathTile", CellType.SPACE);
        put("WallTile", CellType.WALL);
        put("PillTile", CellType.PILL);
        put("GoldTile", CellType.GOLD);
        put("IceTile", CellType.ICE);
        put("PacTile", CharacterType.PACMAN);
        put("TrollTile", CharacterType.M_TROLL);
        put("TX5Tile", CharacterType.M_TX5);
        put("PortalWhiteTile", CellType.PORTAL_WHITE);
        put("PortalYellowTile", CellType.PORTAL_YELLOW);
        put("PortalDarkGoldTile", CellType.PORTAL_DARK_GOLD);
        put("PortalDarkGrayTile", CellType.PORTAL_DARK_GRAY);
    }};


    /**
     * Creates a map using the internal representation of `Map Editor View Controller`.
     *
     * @param mazeArray The map array made up of internal representations.
     */
    public EditorMap(char[][] mazeArray) {
        this.map = new ActorType[mazeArray.length][mazeArray[0].length];

        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                this.map[i][j] = editorRepresentationDictionary.getOrDefault(mazeArray[i][j], CellType.SPACE);
            }
        }
    }

    /**
     * Creates a map from a given file path. The file should be encoded in a xml file.
     *
     * @param filePath The absolute or relative path of the file, with working directory at the top level. (The folder with `pacman`).
     */
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

                    this.map[y][x] = xmlRepresentationDictionary.getOrDefault(cellValue, CellType.SPACE);
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
     * @param loc The target location.
     *
     * @return Whether the item at the given location is an ice cube.
     */
    public boolean isCellType(Location loc) {
        return map[loc.y][loc.x] instanceof CellType;
    }

    @Override
    public HashMap<Integer, ActorType> readMyItemLocations(MapReader reader) {
        return reader.getItemLocations(this);
    }

    @Override
    public HashMap<Integer, ActorType> readMyCharacterLocations(MapReader reader) {
        return reader.getCharacterLocations(this);
    }

    /**
     * @param loc The target location.
     *
     * @return Whether the item at the given location is a character ie, actors that can move.
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
     * check if we can reach a cell from a start location, only consider walls as obstacles
     * @param from start location
     * @param to to location
     * @return true if accessible, false if not
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
