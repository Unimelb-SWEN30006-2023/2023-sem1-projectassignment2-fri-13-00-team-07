import matachi.mapeditor.editor.Controller;

import java.io.File;

public class MapReader {
    private static MapReader instance = null; // singleton
    public static MapReader getInstance() {
        if (instance == null)
            instance = new MapReader();
        return instance;
    }

    // an intermediate converter
    public CellType[][] readMap(String mapName) {
        File mapFile = new File(mapName);
        char[][] tmpMap = (new Controller(mapFile)).getModel().getMap();
        //System.out.println(tmpMap);

    }
}
