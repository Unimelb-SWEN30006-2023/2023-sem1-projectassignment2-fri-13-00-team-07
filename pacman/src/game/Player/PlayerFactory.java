package game.Player;

import game.Level;

import java.util.ArrayList;


/**
 * The Singleton factory to create the pacPlayer according to its mode (isAuto).
 */
public class PlayerFactory {

    public static final int NB_SPRITES = 4;
    private static PlayerFactory instance;

    /**
     * Gets the single instance of the PlayerFactory.
     * @return The single instance of the PlayerFactory.
     */
    public static PlayerFactory getInstance() {
        if (instance == null) {
            instance = new PlayerFactory();
        }
        return instance;
    }

    /**
     * Creates a pacPlayer according to the specifications.
     *
     * @param isAuto: Whether it is an auto-PacPlayer.
     * @param seed: The seed for random move.
     * @param propertyMoves: An ArrayList of Strings representing the moves
     *                       provided in the properties file;
     *                       If null, then ignores the moves in the properties file.
     * @param level: The level for this PacPlayer.
     *
     * @return The created pacPlayer.
     */
    /* The `propertyMoves` argument is for preserving the original game's behavior */
    public PacPlayer createPlayer(boolean isAuto, int seed, ArrayList<String> propertyMoves, Level level) {
        if (isAuto) { // auto PacPlayer
            AutoPacPlayer player = new AutoPacPlayer(true, NB_SPRITES, seed);
            if (propertyMoves != null)
                player.setPropertyMoves(propertyMoves);

            return player;

        } else { // user-controlled PacPlayer
            ManualPacPlayer player = new ManualPacPlayer(true, NB_SPRITES, seed);
            level.addKeyRepeatListener(player);
            return player;
        }
    }

}
