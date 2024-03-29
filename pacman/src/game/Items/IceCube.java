package game.Items;

/**
 * An ice cube - a special type of item with an effect on the monsters' state.
 */
public class IceCube extends Item {

    private static final String IMAGE_FILE = "pacman/sprites/ice.png";

    /**
     * Creates an ice cube.
     */
    public IceCube() {
        super(IMAGE_FILE, CellType.ICE);
    }

    /**
     * The ice cube is eatable. This method would always returns `true`.
     */
    @Override
    public boolean isEatable() {
        return true;
    }
}
