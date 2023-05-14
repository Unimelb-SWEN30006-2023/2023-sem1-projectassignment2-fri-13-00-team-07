package game;

import ch.aplu.jgamegrid.*;

/**
 * Grid for a PacMan Game.
 */

public class PacManGameGrid {
    private final char[][] mazeArray;
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
        this(Game.getNumHorzCells(), Game.getNumVertCells());
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
    public CellType getCellType(Location location) {
        return CellType.valueOfCellChar(getCellChar(location));
    }

    /**
     * Checks if the location is in bound of the maze.
     * @param location: location to be checked
     * @return true if it's in bound, false otherwise.
     */
    public boolean isInBound(Location location) {
        return location.x >= 0 && location.x < mazeArray[0].length
                && location.y >= 0 && location.y < mazeArray.length;
    }
}
