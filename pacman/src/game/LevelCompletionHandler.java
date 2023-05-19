package game;

/**
 * The closure to be called when a level completes with success.
 */
public interface LevelCompletionHandler {

    /**
     * The handler called when the level completes with success.
     *
     * @param game The game on which the level is based.
     */
    void handler(Game game);

}
