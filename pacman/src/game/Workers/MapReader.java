package game.Workers;

import game.ActorType;
import game.Maps.PacManMap;

import java.util.HashMap;

public interface MapReader {
    HashMap<Integer, ActorType> getItemLocations(PacManMap map);
    HashMap<Integer, ActorType> getCharacterLocations(PacManMap map);
}
