package game.Player;

import game.CharacterType;
import game.Level;

import java.util.ArrayList;

public class PlayerFactory {

    public static final int NB_SPRITES = 4;
    private static PlayerFactory instance;

    public static PlayerFactory getInstance() {
        if (instance == null) {
            instance = new PlayerFactory();
        }
        return instance;
    }


    public Player createPlayer(boolean isAuto, int seed, ArrayList<String> propertyMoves, Level level) {
        if (isAuto) {
            AutoPlayer player = new  AutoPlayer(true, NB_SPRITES, seed, CharacterType.PACMAN);
            player.setPropertyMoves(propertyMoves);
            return player;
        } else {
            ManualPlayer player = new ManualPlayer(true, NB_SPRITES, seed, CharacterType.PACMAN);
            level.addKeyRepeatListener(player);
            return player;
        }
    }

}
