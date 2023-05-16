package game.Monsters;

import game.CharacterType;

/**
 * Troll - a special type of monster that walks randomly.
 */
public class Troll extends Monster {


    /**
     * Creates a Troll.
     * @param seed: the seed for random behaviors of the monster
     */
    public Troll(int seed) {
        super(seed, CharacterType.M_TROLL);
    }

    /**
     * Sets a direction for Troll.
     * Troll's walk approach: random walking
     */
    @Override
    protected void setNextDirection() {
        setRandomMoveDirection(getDirection());
    }
}
