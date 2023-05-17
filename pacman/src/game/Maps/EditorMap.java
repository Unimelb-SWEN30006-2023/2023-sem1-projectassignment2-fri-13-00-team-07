package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.CharacterType;
import game.Items.CellType;
import game.MovingActor;

import java.util.HashMap;


public class EditorMap implements PacManMap {
    private final ActorType[][] map;
    private String name; //Ziming: I want a name here

    private final static HashMap<Character, ActorType> editorRepresentationDictionary = new HashMap<Character, ActorType>() {{
        put('a', CellType.SPACE);
        put('b', CellType.WALL);
        put('c', CellType.PILL);
        put('d', CellType.GOLD);
        put('e', CellType.ICE);
        put('f', CharacterType.PACMAN);
        put('g', CharacterType.M_TROLL);
        put('h', CharacterType.M_TX5);
        put('i', CellType.PORTAL_WHITE);
        put('j', CellType.PORTAL_YELLOW);
        put('k', CellType.PORTAL_DARK_GOLD);
        put('l', CellType.PORTAL_DARK_GRAY);
    }};


    public EditorMap(ActorType[][] map) {
        this.map = map;
    }

    public EditorMap(char[][] mazeArray) {
        this.map = new ActorType[mazeArray.length][mazeArray[0].length];

        for (int i = 0; i < mazeArray.length; i++) {
            for (int j = 0; j < mazeArray[0].length; j++) {
                this.map[i][j] = editorRepresentationDictionary.getOrDefault(mazeArray[i][j], CellType.SPACE);
            }
        }
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public ActorType getTypeAt(Location loc) {
        return map[loc.y][loc.x];
    }

    public boolean isCellType(Location loc) {
        return map[loc.y][loc.x] instanceof CellType;
    }

    public boolean isCharacterType(Location loc){
        return map[loc.y][loc.x] instanceof CharacterType;
    }

    @Override
    public int getVerticalCellsCount() {
        return map.length;
    }

    @Override
    public int getHorizontalCellsCount() {
        return map[0].length;
    }

    /**
     * check if we can reach a cell from a start location, only consider walls as obstacles
     * @param from start location
     * @param to to location
     * @return true if accessible, false if not
     */
    public boolean canReach(Location from, Location to){
        return MovingActor.findOptimalPath(from, to, this) != null;
    }

    @Override
    public boolean isWallAt(Location location) {
        return map[location.y][location.x] == CellType.WALL;
    }

}
