package game;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LevelChecker extends Checker{
    private static LevelChecker instance = null;

    public LevelChecker() {;}

    public static Checker getInstance() {
        if (instance == null) {
            instance = new LevelChecker();
        }
        return instance;
    }

    private static String locationStringBuilder(ArrayList<Location> locList){
        String str = "";
        for(int i = 0; i < locList.size(); i++){
            str += ("(" + locList.get(i).x + "," + locList.get(i).y + ")");
            if(i != locList.size() - 1){
                str += ";";
            }
        }
        return str;
    }

    public static void verifyPacStartPoint(EditorMap map, ArrayList<String> errors){
        ArrayList<Location> pacStarts = new ArrayList<>();
        for(int i = 0; i < map.getNumRows(); i++){
            for(int j = 0; j < map.getNumCols(); j++){
                Location loc = new Location(i, j);
                if(map.isCharacterType(loc) && map.getTypeAt(loc) == CharacterType.PACMAN){
                    pacStarts.add(loc);
                }
            }
        }
        if(pacStarts.size() == 0){
            errors.add(map.getName() + ".xml" + " – no start for PacMan");
        }
        else if(pacStarts.size() > 1){
            String errorStr = map.getName() + ".xml" + " – more than one start for Pacman: " + locationStringBuilder(pacStarts);
            errors.add(errorStr);
        }
    }

    public static void verifyPortalTile(EditorMap map, ArrayList<String> errors){
        HashMap<CellType, ArrayList<Location>> hm = new HashMap<>();
        for(int i = 0; i < map.getNumRows(); i++){
            for(int j = 0; j < map.getNumCols(); j++){
                Location loc = new Location(i, j);
                if(map.isCellType(loc) && ((CellType)map.getTypeAt(loc)).getCellChar() == 'p'){
                    CellType type = (CellType)map.getTypeAt(loc);
                    if(!hm.containsKey(type)){
                        ArrayList<Location> lst = new ArrayList<>();
                        lst.add(loc);
                        hm.put(type, lst);
                    }
                    else{
                        hm.get(type).add(loc);
                    }
                }
            }
        }
        // build error string
        for(CellType type:hm.keySet()){
            ArrayList<Location> lst = hm.get(type);
            if(lst.size() != 2){
                errors.add(map.getName() + ".xml" + " - " + type.getName() + " count is not 2: " + locationStringBuilder(lst));
            }
        }
    }

    public static void verifyGoldPill(EditorMap map, ArrayList<String> errors){
        int counter = 0;
        for(int i = 0; i < map.getNumRows(); i++){
            for(int j = 0; j < map.getNumCols(); j++){
                Location loc = new Location(i, j);
                if(map.getTypeAt(loc) == CellType.GOLD || map.getTypeAt(loc) == CellType.PILL) {
                    counter ++;
                }
            }
        }
        if(counter < 2){
            errors.add(map.getName() + " – less than 2 Gold and Pill");
        }
    }

    public static void verifyItemAccessible(EditorMap map, CellType type, ArrayList<String> errors){
        return ;
    }
}
