package game.Maps;

import game.ActorType;

import java.util.HashMap;

public interface MapReader {
    HashMap<Integer, ActorType> getItemLocations(PacManMap map);
    HashMap<Integer, ActorType> getCharacterLocations(PacManMap map);
}
