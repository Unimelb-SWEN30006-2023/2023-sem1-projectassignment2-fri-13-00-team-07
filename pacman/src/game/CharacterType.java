package game;

public enum CharacterType implements ActorType {
    PACMAN("PacMan", "sprites/pacpix_0.gif"),
    M_TROLL("Troll", "sprites/m_troll.gif"),
    M_TX5("TX5", "sprites/m_tx5.gif");

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
