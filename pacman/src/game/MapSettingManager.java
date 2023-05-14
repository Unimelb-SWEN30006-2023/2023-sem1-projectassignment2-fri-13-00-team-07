package game;

public class MapSettingManager extends SettingManager {
    public MapSettingManager(PacManGameGrid grid) {
        super(grid);
    }

    @Override
    public Item createItem(CellType cellType) {
        Item item = null;
        switch (cellType) {
            case PILL -> item = new Pill();
            case GOLD -> item = new Gold();
            case ICE -> item = new IceCube();
        }
        return item;
    }
}
