package game;

public class Portal extends Item implements ActorType {
    private Portal partner;

    public Portal(CellType type, Portal partner) {
        super(image, type);
        this.partner = partner;
    }

}
