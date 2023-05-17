package checker.levelChecks;

import ch.aplu.jgamegrid.Location;
import checker.Check;
import checker.ErrorMessagesBody;
import game.Items.CellType;
import game.Maps.EditorMap;

import java.util.ArrayList;

public class CheckNumGoldPill extends Check implements LevelCheck{

    @Override
    public boolean check(EditorMap map, ArrayList<String> errors) {
        int counter = 0;
        for(int i = 0; i < map.getVerticalCellsCount(); i++){
            for(int j = 0; j < map.getHorizontalCellsCount(); j++){
                Location loc = new Location(j, i);
                if(map.getTypeAt(loc) == CellType.GOLD || map.getTypeAt(loc) == CellType.PILL) {
                    counter ++;
                }
            }
        }
        if(counter < 2){
            errors.add(map.getFileName() + ErrorMessagesBody.LEVEL_C_LESS_TWO_GOLD_PILL);
            return false;
        }
        return true;
    }
}
