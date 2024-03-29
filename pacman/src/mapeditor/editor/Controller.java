package mapeditor.editor;

import ch.aplu.jgamegrid.Location;
import checker.levelChecks.CompositeLevelChecker;
import game.ActorType;
import game.CharacterType;
import game.Game;
import game.Items.CellType;
import game.Maps.EditorMap;
import mapeditor.grid.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller of the application.
 * 
 * @author Daniel "MaTachi" Jonsson
 * @version 1
 * @since v0.0.5
 * 
 */
public class Controller implements ActionListener, GUIInformation {

	/**
	 * The model of the map editor.
	 */
	private Grid model;

	private Tile selectedTile;
	private Camera camera;

	private List<Tile> tiles;

	private GridView grid;
	private View view;

	private int gridWith = Constants.MAP_WIDTH;
	private int gridHeight = Constants.MAP_HEIGHT;
	private String currentMapFile;
	private static final char[] TILE_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'}; // 'a' is default
	private static final String[] TILE_TYPES = {"PathTile", "WallTile", "PillTile",
												"GoldTile", "IceTile", "PacTile",
												"TrollTile", "TX5Tile", "PortalWhiteTile",
												"PortalYellowTile", "PortalDarkGoldTile",
												"PortalDarkGrayTile"
												};
	/* Maps between character and String representations of the tile types */
	private static final HashMap<Character, String> CHAR_TO_STR_DICT = new HashMap<>() {{
		for (int i = 0; i < TILE_CHARS.length; i++) {
			put(TILE_CHARS[i], TILE_TYPES[i]);
		}
	}};

	/* for completeness, mapping the other way around */
	private static final HashMap<String, Character> STR_TO_CHAR_DICT = new HashMap<>() {{
		for (int i = 0; i < TILE_CHARS.length; i++) {
			put(TILE_TYPES[i], TILE_CHARS[i]);
		}
	}};

	/* default data directory */
	private static final String DATA_DIR = "pacman/sprites/editor data/";

	/* Converts ActorType to the internal character representation used by the editor */
	private static final HashMap<ActorType, Character> ACTOR_TYPE_TO_CHAR_DICT = new HashMap<>() {{
		put(CellType.SPACE, 'a');
		put(CellType.WALL, 'b');
		put(CellType.PILL, 'c');
		put(CellType.GOLD, 'd');
		put(CellType.ICE, 'e');
		put(CharacterType.PACMAN, 'f');
		put(CharacterType.M_TROLL, 'g');
		put(CharacterType.M_TX5, 'h');
		put(CellType.PORTAL_WHITE, 'i');
		put(CellType.PORTAL_YELLOW, 'j');
		put(CellType.PORTAL_DARK_GOLD, 'k');
		put(CellType.PORTAL_DARK_GRAY, 'l');
	}};

	/* and the other way around */
	private static final HashMap<Character, ActorType> CHAR_TO_ACTOR_TYPE_DICT = new HashMap<>() {{
		for (Entry<ActorType, Character> entry : ACTOR_TYPE_TO_CHAR_DICT.entrySet()) {
			put(entry.getValue(), entry.getKey());
		}
	}};

	/**
	 * Constructs the controller.
	 */
	public Controller() {
		init(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
	}

	/**
	 * Constructs a controller displaying what's at the given file path.
	 * @param filePath: the path of the file to be read from.
	 * @throws IOException
	 * @throws JDOMException
	 */
	public Controller(String filePath) throws IOException, JDOMException {
		this.currentMapFile = filePath;
		EditorMap map = new EditorMap(filePath);
		checkAndShow(map, "Edit mode on a map with failed check", "Warning");
		this.init(map.getHorizontalCellsCount(), map.getVerticalCellsCount());
		view.setSize(map.getHorizontalCellsCount(), map.getVerticalCellsCount());

		loadObjectsFrom(map);
		grid.redrawGrid();
	}

	/**
	 * Load objects from the editor map to the model.
	 * @param map: the map to be read from.
	 */
	private void loadObjectsFrom(EditorMap map) {
		for (int y = 0; y < map.getVerticalCellsCount(); y++) {
			for (int x = 0; x < map.getHorizontalCellsCount(); x++) {
				model.setTile(x, y, ACTOR_TYPE_TO_CHAR_DICT.get(map.getTypeAt(new Location(x, y))));
			}
		}
	}

	private void init(int width, int height) {
		this.tiles = TileManager.getTilesFromFolder(DATA_DIR);
		this.model = new GridModel(width, height, tiles.get(0).getCharacter());
		this.camera = new GridCamera(model, Constants.GRID_WIDTH,
				Constants.GRID_HEIGHT);

		grid = new GridView(this, camera, tiles); // Every tile is
													// 30x30 pixels

		this.view = new View(this, camera, grid, tiles);
	}

	/**
	 * Different commands that comes from the view.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		for (Tile t : tiles) {
			if (e.getActionCommand().equals(
					Character.toString(t.getCharacter()))) {
				selectedTile = t;
				break;
			}
		}

		if (e.getActionCommand().equals("flipGrid")) {
			// view.flipGrid();
		} else if (e.getActionCommand().equals("save")) {
			saveFile();
		} else if (e.getActionCommand().equals("load")) {
			try {
				loadFile();
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(null, exception, "Error loading file",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (e.getActionCommand().equals("update")) {
			updateGrid(gridWith, gridHeight);
		} else if (e.getActionCommand().equals("start_game")) {
			// Code to switch to pacman game
			EditorMap map = new EditorMap(model.getMap(), currentMapFile);
			if (checkAndShow(map, "The map check failed", "Cannot run")) {
				new Game(map);
			}
		}
	}

	/**
	 * Applies level checking to the map,
	 * and shows a dialogue box on failed checks.
	 * @param map: an EditorMap to be checked
	 * @param message: message to be displayed in the dialogue box
	 * @param title: title of the dialogue box
	 * @return true if the checks were passed, false otherwise.
	 */
	private static boolean checkAndShow(EditorMap map, String message, String title) {
		if (new CompositeLevelChecker().check(map))
			return true;

		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		return false;
	}

	private void updateGrid(int width, int height) {
		view.close();
		init(width, height);
		view.setSize(width, height);
	}

	DocumentListener updateSizeFields = new DocumentListener() {

		public void changedUpdate(DocumentEvent e) {
			gridWith = view.getWidth();
			gridHeight = view.getHeight();
		}

		public void removeUpdate(DocumentEvent e) {
			gridWith = view.getWidth();
			gridHeight = view.getHeight();
		}

		public void insertUpdate(DocumentEvent e) {
			gridWith = view.getWidth();
			gridHeight = view.getHeight();
		}
	};

	private void saveFile() {

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"xml files", "xml");
		chooser.setFileFilter(filter);
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);

		int returnVal = chooser.showSaveDialog(null);
		try {
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				Element level = new Element("level");
				Document doc = new Document(level);
				doc.setRootElement(level);

				Element size = new Element("size");
				int height = model.getHeight();
				int width = model.getWidth();
				size.addContent(new Element("width").setText(width + ""));
				size.addContent(new Element("height").setText(height + ""));
				doc.getRootElement().addContent(size);

				for (int y = 0; y < height; y++) {
					Element row = new Element("row");
					for (int x = 0; x < width; x++) {
						char tileChar = model.getTile(x,y);
						String type = CHAR_TO_STR_DICT.getOrDefault(tileChar, "PathTile");
						Element e = new Element("cell");
						row.addContent(e.setText(type));
					}
					doc.getRootElement().addContent(row);
				}
				XMLOutputter xmlOutput = new XMLOutputter();
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter(chooser.getSelectedFile()));
				// level check applied here,
				// because filename used is only known at this point
				checkAndShow(new EditorMap(model.getMap(), chooser.getSelectedFile().getPath()),
						"Saving map with failed check", "Warning");
			}
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Invalid file!", "error",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
		}
	}

	/**
	 * Load the map at path of `currentMap`, or ask user to select one if the argument is null;
	 */
	private void loadFile() throws IOException, JDOMException {

		File selectedFile;
		JFileChooser chooser = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		chooser.setCurrentDirectory(workingDirectory);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			selectedFile = chooser.getSelectedFile();
		} else {
			throw new IOException("Did not read anything!");
		}

		// level checks applied here,
		// because selected file for loading is only known at this point
		this.currentMapFile = selectedFile.getPath();
		EditorMap map = new EditorMap(currentMapFile);
		checkAndShow(map, "Loading map with failed check", "Warning");
		updateGrid(map.getHorizontalCellsCount(), map.getVerticalCellsCount());

		loadObjectsFrom(map);

		grid.redrawGrid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tile getSelectedTile() {
		return selectedTile;
	}

	/**
	 * Gets the editor's mapping between character and ActorType.
	 * @return the HashMap for this mapping.
	 */
	public static HashMap<Character, ActorType> getCharToActorTypeDict() {
		return CHAR_TO_ACTOR_TYPE_DICT;
	}

	/**
	 * Gets the editor's mapping between String and ActorType.
	 * @return the HashMap for this mapping.
	 */
	public static HashMap<String, ActorType> getStrToActorTypeDict() {
		HashMap<String, ActorType> stringActorType = new HashMap<>();
		for (Map.Entry<Character, String> entry : CHAR_TO_STR_DICT.entrySet()) {
			char charKey = entry.getKey();
			stringActorType.put(entry.getValue(), CHAR_TO_ACTOR_TYPE_DICT.get(charKey));
		}
		return stringActorType;
	}
}
