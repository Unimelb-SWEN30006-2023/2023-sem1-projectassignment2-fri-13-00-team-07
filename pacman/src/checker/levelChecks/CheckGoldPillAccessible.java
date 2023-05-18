package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.Check;
import checker.ErrorMessagesBody;
import game.CharacterType;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;

public class CheckGoldPillAccessible extends Check implements LevelCheck{
    @Override
    public boolean check(EditorMap map, ArrayList<String> errors) {
        boolean flag = true;
        ArrayList<Location> golds = new ArrayList<>();
        ArrayList<Location> pills = new ArrayList<>();
        ArrayList<Location> errorGolds = new ArrayList<>();
        ArrayList<Location> errorPills = new ArrayList<>();
        Location pac = null;
        // extract all golds, pills, and pac locations
        for(int i = 0; i < map.getVerticalCellsCount(); i++){
            for(int j = 0; j < map.getHorizontalCellsCount(); j++){
                Location loc = new Location(j, i);
                if(map.getTypeAt(loc) == CellType.GOLD) {
                    golds.add(loc);
                }
                else if(map.getTypeAt(loc) == CellType.PILL){
                    pills.add(loc);
                }
                else if(map.getTypeAt(loc) == CharacterType.PACMAN){
                    // !!! in theory, if run verifyPacStartPoint in advance, there shouldn't be multiple start Location
                    // but for safete concerns, check if there are multiple start here
                    if(pac != null){
                        return false;
                    }
                    pac = loc;
                }
            }
        }
        // !!! in theory, if run verifyPacStartPoint in advance, there shouldn't be no start Location
        // but for safete concerns, check if there are multiple start here
        if(pac == null){
            return false;
        }
        // build gold error string
        for (Location loc:golds){
            //System.out.println(loc.toString());
            if(!map.canReach(pac, loc)){
                errorGolds.add(loc);
            }
        }
        if(errorGolds.size() > 0){
            flag = false;
            errors.add(map.getFileName() + ErrorMessagesBody.LEVEL_D_GOLD_NOT_ACC + semicolonLocationStringBuilder(errorGolds));
        }
        //build pill error String
        for(Location loc:pills){
            if(!map.canReach(pac, loc)){
                errorPills.add(loc);
            }
        }
        if(errorPills.size() > 0){
            flag = false;
            errors.add(map.getFileName() + ErrorMessagesBody.LEVEL_D_PILL_NOT_ACC + semicolonLocationStringBuilder(errorGolds));
        }
        return flag;
    }
}
