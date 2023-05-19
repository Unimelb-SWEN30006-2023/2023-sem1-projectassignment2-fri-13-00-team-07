package game;

import ch.aplu.jgamegrid.Location;


/**
 * An edge with a source and a destination.
 */
class Edge {

    private final Location source;

    private final Location destination;

    /**
     * Creates an edge.
     *
     * @param source one vertex.
     * @param destination the other vertex.
     */
    public Edge(Location source, Location destination) {
        this.source = source;
        this.destination = destination;
    }

    /**
     * @return One vertex.
     */
    public Location getSource() {
        return source;
    }

    /**
     * @return The other vertex.
     */
    public Location getDestination() {
        return destination;
    }
}