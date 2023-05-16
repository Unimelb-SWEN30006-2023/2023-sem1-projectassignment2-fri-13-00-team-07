package checker;

public class ErrorMessagesBody {
    private static ErrorMessagesBody instance = null;
    public final String GAME_NO_MAPS_FOUND = " – no maps found";
    public final String GAME_MULTI_MAPS_SAME_LEVEL = " – multiple maps at same level: ";
    public final String LEVEL_A_NO_START = " - no start for PacMan";
    public final String LEVEL_A_MULTI_START = "- more than one start for Pacman: ";
    public final String LEVEL_B_NOT_TWO_PORTAL = " is not 2: ";
    public final String LEVEL_C_LESS_TWO_GOLD_PILL  = "less than 2 Gold and Pill";
    public final String LEVEL_D_GOLD_NOT_ACC = " - Gold not accessible: ";
    public final String LEVEL_D_PILL_NOT_ACC = " - Pill not accessible: ";

    public ErrorMessagesBody() {;}

    public static ErrorMessagesBody getInstance() {
        if (instance == null) {
            instance = new ErrorMessagesBody();
        }
        return instance;
    }
}
