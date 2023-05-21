package game.Player;

import game.Level;

import java.util.ArrayList;


/**
 * The factory to create pacPlayer according to its state.
 */
public class PlayerFactory {

    public static final int NB_SPRITES = 4;
    private static PlayerFactory instance;

    /**
     * @return The only instance.
     */
    public static PlayerFactory getInstance() {
        if (instance == null) {
            instance = new PlayerFactory();
        }
        return instance;
    }

    /**
     * Creates a pacPlayer.
     *
     * @param isAuto Whether it is an auto-pacPlayer.
     * @param seed The seed for random move.
     * @param propertyMoves The moves provided in the property file. encoded in internal representation. can be `null`.
     * @param level The level on which the pacPlayer is spawned.
     *
     * @return The created pacPlayer.
     */
    public PacPlayer createPlayer(boolean isAuto, int seed, ArrayList<String> propertyMoves, Level level) {
        if (isAuto) {
            AutoPacPlayer player = new AutoPacPlayer(true, NB_SPRITES, seed);
            if (propertyMoves != null)
                player.setPropertyMoves(propertyMoves);

            return player;
        } else {
            ManualPacPlayer player = new ManualPacPlayer(true, NB_SPRITES, seed);
            level.addKeyRepeatListener(player);
            return player;
        }
    }

}
