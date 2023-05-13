import src.Game;

public class PacManGameAdapter implements GameAdapter {
    private Game game;

    @Override
    public void runGame(String gameFolder) {
        game = new Game(gameFolder);
        // game.runAllLevels();
        // game.runLevel(map);
    }
}
