package game;

import java.util.HashMap;

public class Portal extends Item {
    private Portal partner;
    private static final HashMap<CellType, String> sprites = new HashMap<>() {{
            put(CellType.PORTAL_WHITE, "sprites/portal_white.png");
            put(CellType.PORTAL_YELLOW, "sprites/portal_yellow.png");
            put(CellType.PORTAL_DARK_GOLD, "sprites/portal_dark_gold.png");
            put(CellType.PORTAL_DARK_GRAY, "sprites/portal_dark_gray.png");
    }};

    public Portal(CellType type, Portal partner) {
        super(sprites.get(type), type);
        this.partner = partner;
    }
}
