import game.ActorType;

public interface EditorAdapter {
    void runEditor(String filename); // specified current map, or null for no current map
    boolean applyLevelCheck();

    ActorType[][] getMap(String mapFile);

    void setMap(String mapFile);

}
