package game.Player;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;
import game.Workers.ItemManager;
import game.Level;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * The smart auto pacPlayer.
 */
public class AutoPacPlayer extends PacPlayer {

    private ArrayList<String> propertyMoves;

    // whether the pacActor can move in this simulation iteration

    /**
     * Creates a moving actor based on one or more sprite images.
     *
     * @param isRotatable  if true, the actor's image may be rotated when the direction changes
     * @param nbSprites    the number of sprite images for the same actor
     * @param seed         the seed for random behaviors of the actor
     */
    protected AutoPacPlayer(boolean isRotatable, int nbSprites, int seed) {
        super(isRotatable, nbSprites, seed);
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
        setShouldMove(true);
        ItemManager itemManager = ((Level) gameGrid).getSettingManager().getItemManager();

        PathFindingStrategy pathFindingStrategy = new OptimalPathFindingStrategy();
        LinkedList<Location> path =
                pathFindingStrategy.findPath(
                        getLocation(),
                        (i, expert) -> expert.getTypeAt(i).equals(CellType.PILL) || expert.getTypeAt(i).equals(CellType.GOLD),
                        itemManager,
                        ((Level) gameGrid).getMonsters()
                );

        if (path != null && !path.isEmpty()) {
            Location target = path.remove(0);
            assert getLocation().getDistanceTo(target) == 1;
            setDirectionToTarget(target);
            return;
        } else {
            path = pathFindingStrategy.findPath(
                    this.getLocation(),
                    (i, expert) -> expert.getTypeAt(i).equals(CellType.PILL) || expert.getTypeAt(i).equals(CellType.GOLD),
                    itemManager
            );

            if (path != null && !path.isEmpty()) {
                Location target = path.remove(0);
                assert getLocation().getDistanceTo(target) == 1;
                setDirectionToTarget(target);
                return;
            }
        }

        setRandomMoveDirection(getDirection());
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
                    setShouldMove(true);
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
}
