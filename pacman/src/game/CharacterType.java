package game;

/**
 * A character type.
 */
public enum CharacterType implements ActorType {

    PACMAN("PacMan", "pacman/sprites/pacpix.gif"),
    M_TROLL("Troll", "pacman/sprites/m_troll.gif"),
    M_TX5("TX5", "pacman/sprites/m_tx5.gif");

    private final String name;
    private final String filePath;
    public static final CharacterType[] CHARACTER_TYPES = {PACMAN, M_TROLL, M_TX5};

    CharacterType(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return The file path for the icon of the character.
     */
    public String getFilePath() {
        return filePath;
    }
}
