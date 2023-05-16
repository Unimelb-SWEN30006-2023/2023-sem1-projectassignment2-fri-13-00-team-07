import game.ActorType;

public interface EditorAdapter {
    void runEditor(String filename); // specified current map, or null for no current map
    boolean applyLevelCheck();

    public ActorType[][] getMap(String mapFile);

}
