package checker;

/**
 * 'Body' of the Error messages
 * - contains all the error messages required for error-logging.
 */
public class ErrorMessageBody {
    public static final String GAME_INVALID_DIR = "The directory you specified is invalid";
    public static final String GAME_FAIL_IO = "Some IO exceptions are raised when reading map names";
    public static final String GAME_NO_MAPS_FOUND = " – no maps found";
    public static final String GAME_MULTI_MAPS_SAME_LEVEL = " – multiple maps at same level: ";
    public static final String LEVEL_A_NO_START = " - no start for PacMan";
    public static final String LEVEL_A_MULTI_START = "- more than one start for Pacman: ";
    public static final String LEVEL_B_NOT_TWO_PORTAL = " count is not 2: ";
    public static final String LEVEL_C_LESS_TWO_GOLD_PILL  = " - less than 2 Gold and Pill";
    public static final String LEVEL_D_GOLD_NOT_ACC = " - Gold not accessible: ";
    public static final String LEVEL_D_PILL_NOT_ACC = " - Pill not accessible: ";
}
