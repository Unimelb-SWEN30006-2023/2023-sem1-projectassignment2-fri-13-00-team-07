package checker;

import ch.aplu.jgamegrid.Location;
import game.CharacterType;
import game.EditorMap;
import game.Items.CellType;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelChecker extends Checker {
    private static LevelChecker instance = null;

    public LevelChecker() {
        super();
    }

    public static LevelChecker getInstance() {
        if (instance == null) {
            instance = new LevelChecker();
        }
        return instance;
    }

    private static void verifyPacStartPoint(EditorMap map, ArrayList<String> errors){
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
            errors.add(map.getName() + ".xml" + errorMessagesBody.LEVEL_A_NO_START);
        }
        else if(pacStarts.size() > 1){
            String errorStr = map.getName() + ".xml" + errorMessagesBody.LEVEL_A_MULTI_START + locationStringBuilder(pacStarts);
            errors.add(errorStr);
        }
    }

    private static void verifyPortalTile(EditorMap map, ArrayList<String> errors){
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
                errors.add(map.getName() + ".xml" + " - " + type.getName() + errorMessagesBody.LEVEL_B_NOT_TWO_PORTAL + locationStringBuilder(lst));
            }
        }
    }

    private static void verifyGoldPill(EditorMap map, ArrayList<String> errors){
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
            errors.add(map.getName() + errorMessagesBody.LEVEL_C_LESS_TWO_GOLD_PILL);
        }
    }

    private static void verifyItemAccessible(EditorMap map, ArrayList<String> errors){
        ArrayList<Location> golds = new ArrayList<>();
        ArrayList<Location> pills = new ArrayList<>();
        ArrayList<Location> errorGolds = new ArrayList<>();
        ArrayList<Location> errorPills = new ArrayList<>();
        Location pac = null;
        // extract all golds, pills, and pac locations
        for(int i = 0; i < map.getNumRows(); i++){
            for(int j = 0; j < map.getNumCols(); j++){
                Location loc = new Location(i, j);
                if(map.getTypeAt(loc) == CellType.GOLD) {
                    golds.add(loc);
                }
                else if(map.getTypeAt(loc) == CellType.PILL){
                    pills.add(loc);
                }
                else if(map.getTypeAt(loc) == CharacterType.PACMAN){
                    // !!! in theory, if run verifyPacStartPoint in advance, there shouldn't be multiple start Location
                    // or no start location
                    // but for safete concern, check if there are multiple start here
                    if(pac != null){
                        return;
                    }
                    pac = loc;
                }
            }
        }
        // !!! if no pac start, stop immediately
        if(pac == null){
            return;
        }
        // build gold error string
        for (Location loc:golds){
            if(!map.canReach(pac, loc)){
                errorGolds.add(loc);
            }
        }
        errors.add(map.getName() + ".xml" + errorMessagesBody.LEVEL_D_GOLD_NOT_ACC + locationStringBuilder(errorGolds));
        //build pill error String
        for(Location loc:pills){
            if(!map.canReach(pac, loc)){
                errorPills.add(loc);
            }
        }
        errors.add(map.getName() + ".xml" + errorMessagesBody.LEVEL_D_PILL_NOT_ACC + locationStringBuilder(errorGolds));
    }

    public boolean checkLevel(EditorMap map){
        ArrayList<String> errors = new ArrayList<>();
        verifyPacStartPoint(map, errors);
        verifyPortalTile(map, errors);
        verifyGoldPill(map, errors);
        verifyItemAccessible(map, errors);
        if(errors.size() == 0){
            return true;
        }
        else{
            logErrors(errors);
            return false;
        }
    }


}
