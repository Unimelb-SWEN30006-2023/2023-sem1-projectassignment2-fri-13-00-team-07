package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.Check;
import checker.ErrorMessagesBody;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckPortalPair extends Check implements LevelCheck{
    @Override
    public boolean check(EditorMap map, ArrayList<String> errors) {
        boolean flag = true;
        HashMap<CellType, ArrayList<Location>> hm = new HashMap<>();
        for(int i = 0; i < map.getVerticalCellsCount(); i++){
            for(int j = 0; j < map.getHorizontalCellsCount(); j++){
                Location loc = new Location(j, i);
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
                flag = false;
                errors.add(map.getFileName() + " - " + type.getName() + ErrorMessagesBody.LEVEL_B_NOT_TWO_PORTAL + semicolonLocationStringBuilder(lst));
            }
        }
        return flag;
    }
}
