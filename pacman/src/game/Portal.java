package game;

public class Portal extends Item {
    private Portal partner;

    public Portal(String image, Portal partner) {
        super(image, CellType.PORTAL);
        this.partner = partner;
    }
}
