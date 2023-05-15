package game;

import ch.aplu.jgamegrid.Location;

import java.util.HashMap;

public interface MapReader {
    HashMap<Location, ActorType> getItemLocations();
    HashMap<Location, ActorType> getCharacterLocations();
    PacManMap getMap();
}
