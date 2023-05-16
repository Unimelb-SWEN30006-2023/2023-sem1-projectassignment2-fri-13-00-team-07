package game;

import ch.aplu.jgamegrid.Location;

 class Edge {

    Location source;

    Location destination;

    public Edge(Location source, Location destination) {
        this.source = source;
        this.destination = destination;
    }
}