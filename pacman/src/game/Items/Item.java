package game.Items;

import ch.aplu.jgamegrid.Actor;

import java.awt.*;

/**
 * Abstract class for an item - a stationary actor with an effect on the game.
 * Each item has a defined location and cell type, thus also a name and color
 * (see the CellType enum).
 */
public abstract class Item extends Actor {
    private static final int FILL_CIRCLE_RADIUS = 5;
    private final CellType type;

    /**
     * Creates an item based on the given image and cell type.
     * @param image: the path or URL to the image file displayed for this item
     * @param type: the type of the item's cell
     */
    public Item(String image, CellType type) {
        super(image);
        this.type = type;
    }

    /**
     * Creates an item without a sprite image
     * @param type: the type of the item's cell
     */
    public Item(CellType type) {
        super();
        this.type = type;
    }

    /**
     * Gets the fill circle radius for all items.
     * @return an integer representing the fill circle radius.
     */
    public static int getFillCircleRadius() {
        return FILL_CIRCLE_RADIUS;
    }

    /**
     * Gets the color for this item.
     * @return its color.
     */
    public Color getColor() {
        return type.getColor();
    }

    /**
     * Gets the name for this item.
     * @return its name.
     */
    public String getName() {
        return type.toString();
    }

    /**
     * Gets the type for this item's cell.
     * @return its cell type.
     */
    public CellType getType() {
        return type;
    }

    /**
     * Checks if the item is eatable.
     * @return true if the item is eatable, false otherwise.
     */
    public abstract boolean isEatable();

    /**
     * Checks if the given object is equal to this item
     * (i.e. same type and location).
     * @param other: the object to be compared to this item
     * @return true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Item otherItem = (Item) other;
        return (otherItem.type.equals(this.type)
                && otherItem.getLocation().equals(this.getLocation()));
    }
}
