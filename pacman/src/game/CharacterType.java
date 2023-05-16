package game;

public enum CharacterType implements ActorType {
    PACMAN("PacMan", "pacman/sprites/pacpix.gif"),
    M_TROLL("Troll", "pacman/sprites/m_troll.gif"),
    M_TX5("TX5", "pacman/sprites/m_tx5.gif");

    private final String name;
    private final String filePath;

    CharacterType(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }
}
