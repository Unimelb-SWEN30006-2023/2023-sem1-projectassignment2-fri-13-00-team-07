package game;

/**
 * A gold - a special type of pill
 * with a larger score effect than a 'plain' pill,
 * and can affect the monsters' state.
 */
public class Gold extends Pill {

    private static final String IMAGE_FILE = "sprites/gold.png";
    private static final int SCORE_EFFECT = 5;

    /**
     * Creates a gold.
     */
    public Gold() {
        super(IMAGE_FILE, CellType.GOLD, SCORE_EFFECT);
    }
}
