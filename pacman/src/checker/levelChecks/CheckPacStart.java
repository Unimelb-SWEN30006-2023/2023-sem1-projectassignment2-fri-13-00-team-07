package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.Check;
import checker.ErrorMessagesBody;
import game.CharacterType;
import game.Maps.EditorMap;

import java.util.ArrayList;

/**
 * check if there is one and only one pac start location
 */
public class CheckPacStart extends Check implements LevelCheck{

    @Override
    public boolean check(EditorMap map, ArrayList<String> errors) {
        boolean flag = true;
        ArrayList<Location> pacStarts = new ArrayList<>();
        for(int i = 0; i < map.getVerticalCellsCount(); i++){
            for(int j = 0; j < map.getHorizontalCellsCount(); j++){
                Location loc = new Location(j, i);
                if(map.isCharacterType(loc) && map.getTypeAt(loc) == CharacterType.PACMAN){
                    pacStarts.add(loc);
                }
            }
        }
        if(pacStarts.size() == 0){
            flag = false;
            errors.add(map.getFileName() + ErrorMessagesBody.LEVEL_A_NO_START);
        }
        else if(pacStarts.size() > 1){
            String errorStr = map.getFileName() + ErrorMessagesBody.LEVEL_A_MULTI_START + semicolonLocationStringBuilder(pacStarts);
            flag = false;
            errors.add(errorStr);
        }
        return flag;
    }
}
