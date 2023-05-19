package game.Items;

import ch.aplu.jgamegrid.Location;

import java.util.HashMap;

/**
 * The portal, when an actor land one a portal, it would be teleported to its `partnerLocation`.
 */
public class Portal extends Item {

    private final Location partnerLocation;

    private static final HashMap<CellType, String> sprites = new HashMap<>() {{
            put(CellType.PORTAL_WHITE, "pacman/sprites/portal_white.png");
            put(CellType.PORTAL_YELLOW, "pacman/sprites/portal_yellow.png");
            put(CellType.PORTAL_DARK_GOLD, "pacman/sprites/portal_darkGold.png");
            put(CellType.PORTAL_DARK_GRAY, "pacman/sprites/portal_darkGray.png");
    }};

    /**
     * Creates the portal.
     *
     * @param type The cell type, which determines the color of the portal.
     * @param partnerLocation The partner location for this portal.
     */
    public Portal(CellType type, Location partnerLocation) {
        super(sprites.get(type), type);
        this.partnerLocation = partnerLocation;
    }

    /**
     * @return The location of its partner.
     */
    public Location getPartnerLocation() {
        return partnerLocation;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEatable() {
        return false;
    }
}
