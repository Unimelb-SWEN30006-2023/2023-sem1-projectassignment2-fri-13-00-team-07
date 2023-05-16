package game.Monsters;

import ch.aplu.jgamegrid.Location;
import game.CharacterType;
import game.Level;

import java.util.Timer;
import java.util.TimerTask;

public class TX5 extends Monster {
    private static final int WAITING_TIME = 5;
    private boolean waiting = true;

    /**
     * Creates a TX5.
     * @param seed: the seed for random behaviors of the monster
     */
    public TX5(int seed) {
        super(seed, CharacterType.M_TX5);

        // Set up the timer for waiting, when the game starts
        Timer timer = new Timer();
        int SECOND_TO_MILLISECONDS = 1000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                waiting = false;
            }
        }, (long) WAITING_TIME * SECOND_TO_MILLISECONDS);
    }

    /**
     * Sets a direction for T-X5.
     * TX5's walk approach:
     * 1. Walk towards PacMan if it can,
     *    provided that the location is valid and not recently visited.
     * 2. Otherwise, randomly walk like Troll.
     */
    @Override
    protected void setNextDirection() {
        double oldDirection = getDirection();
        Location pacLocation = ((Level) gameGrid).getPacActor().getLocation();
        // Try to move towards the PacActor
        setDirectionToTarget(pacLocation);
        if (!isVisited(getNextMoveLocation()) && isMoveValid())
            return;
        // Second resort: random walk
        setRandomMoveDirection(oldDirection);
    }

    /**
     * Called in every simulation iteration.
     */
    @Override
    public void act() {
        if (waiting) // do nothing
            return;
        super.act();
    }
}
