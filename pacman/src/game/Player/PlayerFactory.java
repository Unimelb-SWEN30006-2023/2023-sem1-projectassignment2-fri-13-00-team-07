package game.Player;

import game.CharacterType;
import game.Level;

import java.util.ArrayList;


/**
 * The factory to create player according to its state.
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
     * Creates a player.
     *
     * @param isAuto Whether it is an auto-player.
     * @param seed The seed for random move.
     * @param propertyMoves The moves provided in the property file. encoded in internal representation. can be `null`.
     * @param level The level on which the player is spawned.
     *
     * @return The created player.
     */
    public Player createPlayer(boolean isAuto, int seed, ArrayList<String> propertyMoves, Level level) {
        if (isAuto) {
            AutoPlayer player = new  AutoPlayer(true, NB_SPRITES, seed);
            if (propertyMoves != null)
                player.setPropertyMoves(propertyMoves);

            return player;
        } else {
            ManualPlayer player = new ManualPlayer(true, NB_SPRITES, seed);
            level.addKeyRepeatListener(player);
            return player;
        }
    }

}
