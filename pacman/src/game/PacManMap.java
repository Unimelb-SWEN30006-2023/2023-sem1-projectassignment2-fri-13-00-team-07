package game;

import ch.aplu.jgamegrid.Location;

public interface PacManMap {
    ActorType getTypeAt(Location location);
}
