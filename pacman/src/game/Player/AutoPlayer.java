package game.Player;

import ch.aplu.jgamegrid.Location;
import game.CharacterType;
import game.Items.CellType;
import game.Level;
import game.Maps.PacManMap;
import game.MovingActor;

import java.util.ArrayList;
import java.util.LinkedList;

public class AutoPlayer extends Player {

    private ArrayList<String> propertyMoves;

    private LinkedList<Location> currentAutoPath = new LinkedList<>();

    // whether the pacActor can move in this simulation iteration
    private boolean shouldMove = false;

    /**
     * Creates a moving actor based on one or more sprite images.
     *
     * @param isRotatable : if true, the actor's image may be rotated when the direction changes
     * @param nbSprites   : the number of sprite images for the same actor
     * @param seed        : the seed for random behaviors of the actor
     * @param type
     */
    public AutoPlayer(boolean isRotatable, int nbSprites, int seed, CharacterType type) {
        super(isRotatable, nbSprites, seed, type);
    }

    /**
     * Sets the next (valid) direction.
     * Only used when moving in auto mode.
     */
    @Override
    protected void setNextDirection() {
        if (!propertyMoves.isEmpty()) {
            followPropertyMoves();
            return;
        }

        // at this stage: either go to a valid pill, or move randomly
        shouldMove = true;
        PacManMap map = ((Level) gameGrid).getSettingManager();

        if (currentAutoPath == null || currentAutoPath.isEmpty()) {
            currentAutoPath = MovingActor.findOptimalPath(this.getLocation(), i -> map.getTypeAt(i) == CellType.GOLD || map.getTypeAt(i) == CellType.PILL, map);
        }
        if (currentAutoPath != null && !currentAutoPath.isEmpty()) {
            Location target = currentAutoPath.remove(0);
            assert this.getLocation().getDistanceTo(target) == 1;
            this.setDirectionToTarget(target);
        } else {
            // last resort: random walk
            setRandomMoveDirection(this.getDirection());
        }
    }

    /**
     * Sets the direction according to moves from the properties file.
     */
    private void followPropertyMoves() {
        String currentMove = propertyMoves.remove(0);
        switch (currentMove) {
            case "R" -> turn(90);
            case "L" -> turn(-90);
            case "M" -> {
                if (isMoveValid()) {
                    shouldMove = true;
                }
            }
        }
    }

    /**
     * Sets the moves (from the properties file) for the PacActor.
     * @param propertyMoves: ArrayList of Strings, representing the property moves.
     */
    public void setPropertyMoves(ArrayList<String> propertyMoves) {
        this.propertyMoves = propertyMoves;
    }

    @Override
    protected boolean shouldMove() {
        return shouldMove;
    }

    @Override
    protected void resetMove() {
        shouldMove = false;
    }
}