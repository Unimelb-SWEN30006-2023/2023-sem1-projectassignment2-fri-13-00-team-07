package game.Maps;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Maps.PacManMap;

import java.util.HashMap;

public interface MapReader {
    HashMap<Location, ActorType> getItemLocations();
    HashMap<Location, ActorType> getCharacterLocations();

    boolean isWallAt(Location location);
}
