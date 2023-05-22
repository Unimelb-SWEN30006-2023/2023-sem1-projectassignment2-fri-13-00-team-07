package game;

/**
 * The functional interface, representing a handler that is to be called
 * when a level completes with success.
 */
@FunctionalInterface
public interface LevelCompletionHandler {

    /**
     * The handler called when the level completes with success.
     * @param game: The game on which the level is based.
     */
    void handler(Game game);

}
