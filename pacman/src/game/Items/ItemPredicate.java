package game.Items;

import ch.aplu.jgamegrid.Location;

public interface ItemPredicate {

    boolean satisfies(Location location);

}
