package game;

import javax.xml.stream.Location;
import java.util.HashMap;

public interface MapReader {
    HashMap<Location, ActorType> getItemLocations();
    HashMap<Location, ActorType> getCharacterLocations();
    PacManMap getMap();
}
