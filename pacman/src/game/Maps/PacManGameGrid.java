package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
import game.Level;

/**
 * Grid for a PacMan Level.
 */
public class PacManGameGrid implements PacManMap {
    private final char[][] mazeArray;

    // default setting
    private static final String MAZE =  "xxxxxxxxxxxxxxxxxxxx" + // 0
                                        "x....x....g...x....x" + // 1
                                        "xgxx.x.xxxxxx.x.xx.x" + // 2
                                        "x.x.......i.g....x.x" + // 3
                                        "x.x.xx.xx  xx.xx.x.x" + // 4
                                        "x......x    x......x" + // 5
                                        "x.x.xx.xxxxxx.xx.x.x" + // 6
                                        "x.x......gi......x.x" + // 7
                                        "xixx.x.xxxxxx.x.xx.x" + // 8
                                        "x...gx....g...x....x" + // 9
                                        "xxxxxxxxxxxxxxxxxxxx";// 10

    /**
     * Creates a PacManGameGrid of the default PacMan game dimensions.
     */
    public PacManGameGrid() {
        this(Level.DEFAULT_NB_HORZ_CELLS, Level.DEFAULT_NB_VERT_CELLS);
    }

    /**
     * Creates a grid using the internal representation of the grid.
     *
     * @param mazeArray The array in the internal representation.
     */
    public PacManGameGrid(char[][] mazeArray) {
        this.mazeArray = mazeArray;
    }

    /**
     * Creates a PacManGameGrid of the specified dimensions.
     * @param nbHorzCells: integer number of horizontal cells
     * @param nbVertCells: integer number of vertical cells
     */
    public PacManGameGrid(int nbHorzCells, int nbVertCells) {
        mazeArray = new char[nbVertCells][nbHorzCells];
        // Copy structure into integer array
        for (int i = 0; i < nbVertCells; i++) {
            for (int k = 0; k < nbHorzCells; k++) { // store char directly
                mazeArray[i][k] = MAZE.charAt(nbHorzCells * i + k);
            }
        }
    }

    /**
     * Gets the character for the cell at the given location.
     * @param location: location to look up
     * @return the character corresponding to that cell.
     */
    private char getCellChar(Location location) {
        return mazeArray[location.y][location.x];
    }

    /**
     * Gets the cell type at the given location.
     * @param location: location to look up
     * @return one of the defined cell types.
     */
    @Override
    public ActorType getTypeAt(Location location) {
        return CellType.valueOfCellChar(getCellChar(location));
    }

    /** {@inheritDoc} */
    @Override
    public int getHorizontalCellsCount() {
        return Level.DEFAULT_NB_HORZ_CELLS;
    }

    /** {@inheritDoc} */
    @Override
    public int getVerticalCellsCount() {
        return Level.DEFAULT_NB_VERT_CELLS;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWallAt(Location location) {
        return getTypeAt(location) == CellType.WALL;
    }
}
