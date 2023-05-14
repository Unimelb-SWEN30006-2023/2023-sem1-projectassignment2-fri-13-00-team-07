import game.*;
import matachi.mapeditor.editor.*;

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
    public ActorType[][] getMap(String mapFile) {
        editor.setCurrentMap(mapFile);
        setUpConverter();
        char[][] mapWithChars = editor.loadFile();
        int numRows = mapWithChars.length;
        int numCols = mapWithChars[0].length;
        ActorType[][] mapWithActors = new ActorType[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                mapWithActors[i][j] = charToActorType(mapWithChars[i][j]);
            }
        }
        return mapWithActors;
    }

    /**
     * Sets up the converter from the editor's internal representation
     * to the game's representation
     */
    private void setUpConverter() {
        String[] tileStrings = editor.getCharToStrDict().values().toArray(new String[0]);
        ActorType[] actorTypes = {CellType.SPACE, CellType.WALL, CellType.PILL,
                                    CellType.GOLD, CellType.ICE, CharacterType.PACMAN,
                                CharacterType.TROLL_M, CharacterType.TX5_M, CellType.PORTAL_WHITE,
                                CellType.PORTAL_YELLOW, CellType.PORTAL_DARK_GOLD, CellType.PORTAL_DARK_GRAY};
        for (int i = 0; i < tileStrings.length; i++) {
            converter.put(tileStrings[i], actorTypes[i]);
        }
    }

    /**
     * Converts the character (editor's representation) to an
     * ActorType (game's representation).
     * @param c: character representing a tile
     * @return ActorType: the corresponding actor type.
     */
    private ActorType charToActorType(char c) {
        String tileString = editor.getCharToStrDict().get(c);
        return converter.get(tileString);
    }
}
