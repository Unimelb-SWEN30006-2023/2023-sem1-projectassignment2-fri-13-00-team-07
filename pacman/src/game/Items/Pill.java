package game.Items;


/**
 * A specific type of item -
 * One that the PacActor needs to obtain to win,
 * and has an effect on the PacActor's score.
 */
public class Pill extends Item {
    private static final int SCORE_EFFECT = 1; // default for a 'plain' pill
    private final int scoreEffect;

    /**
     * Creates a pill with the given image, cell type and score effect.
     * @param image: the path or URL to the image file displayed for this item
     * @param type: the type of the item's cell
     * @param scoreEffect: the score effect of the item
     */
    public Pill(String image, CellType type, int scoreEffect) {
        super(image, type);
        this.scoreEffect = scoreEffect;
    }

    /**
     * Creates a 'plain' pill by default.
     */
    public Pill() {
        super(CellType.PILL);
        this.scoreEffect = SCORE_EFFECT;
    }

    /**
     * Gets the effect of the pill on the PacActor's score.
     * @return its score effect.
     */
    public int getScoreEffect() {
        return scoreEffect;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEatable() {
        return true;
    }
}
