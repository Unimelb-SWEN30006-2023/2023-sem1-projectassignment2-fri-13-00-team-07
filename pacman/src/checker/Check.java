package checker;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;

/**
 * Abstract Check. Contains helpers for each checks to use.
 */
public abstract class Check {

    /**
     * given a list of string, concat them into a single string, seperated by semicolons
     * @param lst a list of string
     * @return a concat string
     */
    protected String semicolonStringBuilder(ArrayList<String> lst){
        // !!! grid start from (1, 1), not (0, 0) change this later
        String str = "";
        for (int i = 0; i < lst.size(); i++) {
            str += lst.get(i);
            if (i != lst.size() - 1) {
                str += "; ";
            }
        }
        return str;
    }

    /**
     * given a list of location, concat them into a single string, seperated by semicolons
     * !!! add one offset for both x and y, required in spec.
     * this will make the initial location (1, 1) instead of (0, 0)
     * @param locList a list of locations
     * @return a location list string, with semicolon seperated
     */
    protected String semicolonLocationStringBuilder(ArrayList<Location> locList){
        ArrayList<String> locStrLst = new ArrayList<>();
        for (Location loc:locList) {
            locStrLst.add("(" + (loc.x + 1) + "," + (loc.y + 1) + ")");
        }
        return semicolonStringBuilder(locStrLst);
    }
}
