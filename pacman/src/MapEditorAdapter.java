import ch.aplu.jgamegrid.Actor;
import game.ActorType;
import matachi.mapeditor.editor.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MapEditorAdapter implements EditorAdapter {
    private Controller editor = new Controller();
    private HashMap<String, ActorType> converter = new HashMap<>();

    @Override
    public void runEditor(String mapFile) {
        editor.setCurrentMap(mapFile); // if null, then no current map
        editor.run();
    }

    @Override
    public void getMap(String mapFile) {
        editor.setCurrentMap(mapFile);
        char[][] mapArray = editor.loadFile();

    }

    private void setUpConverter() {
        Collection<String> tileStrings = editor.getCharToStrDict().values();
        {"PathTile", "WallTile", "PillTile",
                "GoldTile", "IceTile", "PacTile",
                "TrollTile", "TX5Tile", "PortalWhiteTile",
                "PortalYellowTile", "PortalDarkGoldTile",
                "PortalDarkGrayTile"
        };
        ActorType[] actorTypes = {}
    }

    private ActorType charToActorType(char c) {
        String tileString = editor.getCharToStrDict().get(c);
        return converter.get(tileString);
    }
}
