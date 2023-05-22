package game;

/**
 * The type of actor, could be a character or an item.
 */
public interface ActorType {
    // all Actors have a name

    /**
     * @return The name for this actor.
     */
    String getName();
}
