import game.ActorType;

public interface EditorAdapter {
    void runEditor(String filename); // specified current map, or null for no current map
    ActorType[][] getMap(String filename);

}
