package game;

public enum CharacterType implements ActorType {
    PACMAN("PacMan", "sprites/pacpix_0.gif"),
    TROLL_M("Troll", "sprites/m_troll.gif"),
    TX5_M("TX5", "sprites/m_tx5.gif");

    private final String name;
    private final String filePath;

    CharacterType(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }
}
