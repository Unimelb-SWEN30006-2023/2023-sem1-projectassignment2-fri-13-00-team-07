package game.Items;

import game.ActorType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Cell types, each with an associated character, name, and color.
 */
public enum CellType implements ActorType {

    WALL('x', "Wall", Color.gray),
    PILL('.', "pills", Color.white),
    SPACE(' ', "Space", Color.lightGray),
    GOLD('g', "gold", Color.yellow),
    ICE('i', "ice", Color.blue),
    PORTAL_WHITE('p', "portal White", Color.white),
    PORTAL_DARK_GOLD('p', "portal Dark Gold", new Color(0.71f, 0.58f, 0.06f)),
    PORTAL_DARK_GRAY('p', "portal Dark Grey", Color.darkGray),
    PORTAL_YELLOW('p', "portal Yellow", Color.yellow);


    /* for simplicity of lookups */
    private static final HashMap<Character, CellType> BY_CELL_CHAR = new HashMap<>();
    private static final HashMap<String, CellType> BY_NAME = new HashMap<>();
    private static final HashMap<Color, CellType> BY_COLOR = new HashMap<>();

    static {
        for (CellType s : values()) {
            BY_CELL_CHAR.put(s.cellChar, s);
            BY_NAME.put(s.name, s);
            BY_COLOR.put(s.color, s);
        }
    }

    private final char cellChar;
    private final String name;
    private final Color color;
    /* All portals, with different colors */
    public static final ArrayList<CellType> PORTALS = new ArrayList<>(List.of(new CellType[]{PORTAL_WHITE, PORTAL_DARK_GOLD, PORTAL_DARK_GRAY, PORTAL_YELLOW}));

    /**
     * Creates a cell type.
     * @param cellChar: character representing this cell type
     * @param name: name of this cell type
     * @param color: color for this cell type
     */
    CellType(char cellChar, String name, Color color) {
        this.cellChar = cellChar;
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the cell type corresponding to the given character.
     * @param cellChar: character to look up
     * @return the corresponding cell type.
     */
    public static CellType valueOfCellChar(char cellChar) {
        return BY_CELL_CHAR.get(cellChar);
    }

    /**
     * Gets the cell type corresponding to the given name.
     * @param name: name (String) to look up
     * @return the corresponding cell type.
     */
    public static CellType valueOfName(String name) {
        return BY_NAME.get(name);
    }

    /**
     * Gets the cell type corresponding to the given color.
     * @param color: color to look up
     * @return the corresponding cell type.
     */
    public static CellType valueOfColor(Color color) {
        return BY_COLOR.get(color);
    }


    /* for completeness */

    /**
     * Gets the character representation of the cell type.
     * @return the corresponding character.
     */
    public char getCellChar() {
        return cellChar;
    }

    /**
     * Gets the String representation (name) of the cell type.
     * @return the corresponding String.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the name to the type.
     * @return The name, pretty printed.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the color associated with the cell type.
     * @return the corresponding color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Given the type, determines if it is a portal.
     * @return A boolean determining whether it is a portal.
     */
    public boolean isPortal() {
        return PORTALS.contains(this);
    }
}
