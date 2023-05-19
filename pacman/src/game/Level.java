package game;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import game.Maps.PacManGameGrid;
import game.Maps.PacManMap;
import game.Monsters.Monster;
import game.Monsters.TX5;
import game.Monsters.Troll;
import game.Player.Player;
import game.Player.PlayerFactory;
import game.utility.GameCallback;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * The PacMan Level, which contains all the actors, a property reader,
 * and a setting manager.
 */

public class Level extends GameGrid {
    /* Setting constants */
    public final static int DEFAULT_NB_HORZ_CELLS = 20;
    public final static int DEFAULT_NB_VERT_CELLS = 11;
    private final static int CELL_SIZE = 20;
    private static final int SLOW_DOWN_FACTOR = 3;
    private static final int KEY_REPEAT_PERIOD = 150;
    private static final int SIMULATION_PERIOD = 100;
    private static final Color WIN_COLOR = Color.yellow;
    private static final Color LOSE_COLOR = Color.red;

    /* Actors */
    protected Player player;
    private final ArrayList<Monster> monsters = new ArrayList<>();
    private final SettingManager settingManager;
    private final GameCallback gameCallback;

    private final Optional<LevelCompletionHandler> completionHandler;
    private final Optional<WeakReference<Game>> game;


    private int maxPillsCount = 0;

    // Level creation using properties file only for isAuto and seed, and a separate map
    public Level(Properties properties, PacManMap map, Optional<LevelCompletionHandler> completionHandler, Optional<WeakReference<Game>> game) {
        super(map.getHorizontalCellsCount(), map.getVerticalCellsCount(), CELL_SIZE, false);
        this.gameCallback = new GameCallback();
        this.settingManager = new SettingManager(properties, map, this);

        this.completionHandler = completionHandler;
        this.game = game;
    }


    // Level creation using properties file for setting
    public Level(Properties properties) {
        // Setup game level
        this(properties, new PacManGameGrid(), Optional.empty(), Optional.empty()); // uses the default string map (original behavior)
    }

    public void run() {
        // Initializations
        setSimulationPeriod(SIMULATION_PERIOD);
        setTitle("[PacMan in the Multiverse]");
        setUpActors();

        // Run this level
        doRun();
        show();
    }


    /**
     * Adds a monster to the game.
     * @param monster: monster to add
     */
    private void addMonster(Monster monster, Location location) {
        monsters.add(monster);
        addActor(monster, location, Location.NORTH); // bind it to the game
        monster.setSlowDown(SLOW_DOWN_FACTOR);
    }

    /**
     * Sets up the actors (pacActor and monsters).
     */
    private void setUpActors() {
        int seed = settingManager.getSeed();
        Location pacActorLocation = null;

        HashMap<Location, ActorType> characterLocations = settingManager.getCharacterLocations();
        for (Map.Entry<Location, ActorType> entry : characterLocations.entrySet()) {
            Location location = entry.getKey();
            ActorType type = entry.getValue();
            if (type.equals(CharacterType.PACMAN)) {
                setUpPacActor(seed);
                pacActorLocation = location;
            } else if (type.equals(CharacterType.M_TROLL)) {
                addMonster(new Troll(seed), location);
            } else if (type.equals(CharacterType.M_TX5)) {
                addMonster(new TX5(seed), location);
            }
        }

        // addActor(): actor added last will act and be painted first
        // add pacActor last so that it would `act` first
        if (pacActorLocation != null)
            addActor(player, pacActorLocation);
    }

    private void setUpPacActor(int seed) {
        final var propertyMoves = settingManager.getPlayerMoves();
        final var isAuto = settingManager.getPlayerMode();
        player = PlayerFactory.getInstance().createPlayer(isAuto, seed, propertyMoves, this);

        setKeyRepeatPeriod(KEY_REPEAT_PERIOD);
        player.setSlowDown(SLOW_DOWN_FACTOR);
    }

    public GameCallback getGameCallback() {
        return gameCallback;
    }


    /**
     * Called in every simulation cycle after all actor act() calls.
     */
    @Override
    public void act() {
        if (maxPillsCount == 0)
            maxPillsCount = settingManager.countPills(); // store the pills count

        boolean gameOver = pacActorCollidedWithMonster();
        if (gameOver) {
            setLostEnding();
        } else if (player.getNbPills() >= maxPillsCount) {
            setWinEnding();
            if (completionHandler.isPresent() && game.isPresent()) {
                completionHandler.get().handler(game.get().get());
            }
        }

        super.act();
    }

    /**
     * Gets the setting manager of the game.
     * @return the setting manager.
     */
    public SettingManager getSettingManager() {
        return settingManager;
    }


    /**
     * Gets the pacActor of the game.
     * @return the pacActor.
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Checks whether the pacActor has collided with a monster.
     * @return true if the collision happened, false otherwise.
     */
    private boolean pacActorCollidedWithMonster() {
        for (Monster monster: monsters) {
            if (collide(monster, player)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the two actors collides.
     * @param actor1: first actor
     * @param actor2: second actor
     * @return true if they collided, false otherwise.
     */
    private boolean collide(Actor actor1, Actor actor2) {
        return actor1.getLocation().equals(actor2.getLocation());
    }


    /**
     * Sets the ending for a won game.
     */
    private void setWinEnding() {
        String title = "YOU WIN";
        getBg().setPaintColor(WIN_COLOR);
        setEnding(title);
    }

    /**
     * Sets the ending for a lost game.
     */
    private void setLostEnding() {
        String title = "GAME OVER";
        getBg().setPaintColor(LOSE_COLOR);
        addActor(new Actor("pacman/sprites/explosion3.gif"), player.getLocation());
        setEnding(title);
    }

    /**
     * Sets the ending with the given title.
     * @param title: String for the ending's title
     */
    private void setEnding(String title) {
        setTitle(title);
        doPause();

        gameCallback.endOfGame(title);
    }
}
