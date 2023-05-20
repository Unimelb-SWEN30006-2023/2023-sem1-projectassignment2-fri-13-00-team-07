package game.Maps;

import game.ActorType;

import java.util.HashMap;

public interface MapReader {
    HashMap<Integer, ActorType> getItemLocations();
    HashMap<Integer, ActorType> getCharacterLocations();
    PacManMap getMap();
}
