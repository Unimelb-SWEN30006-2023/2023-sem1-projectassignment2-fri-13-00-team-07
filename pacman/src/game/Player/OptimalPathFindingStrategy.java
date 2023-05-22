package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
import game.Items.LocationPredicate;
import game.LocationExpert;
import game.Workers.LocationIndexConverter;
import game.Monsters.Monster;

import java.util.*;
import java.util.stream.IntStream;

/**
 * An optimal path finding strategy, using Breadth-First-Search
 */

public class OptimalPathFindingStrategy implements PathFindingStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedList<Location> findPath(Location source, LocationPredicate predicate,
                                         LocationExpert locationExpert, ArrayList<Monster> monsters) {
        LocationIndexConverter indexConverter = new LocationIndexConverter(locationExpert.getHorizontalCellsCount());

        final HashMap<CellType, ArrayList<Location>> portalLocations = locationExpert.getPortalLocations();
        final HashSet<Integer> visitedSet = new HashSet<>();
        final LinkedList<Edge> paths = new LinkedList<>();
        LinkedList<Location> queue = new LinkedList<>();

        // enqueue the source
        queue.add(source);
        markLocationAsVisited(source, visitedSet, indexConverter);

        while (!queue.isEmpty()) {
            // while possible, dequeue
            Location vertex = queue.remove();

            if (predicate.satisfies(vertex, locationExpert)) { // if is destination, return
                LinkedList<Location> result = new LinkedList<>();

                result.add(vertex);
                Location destination = vertex;
                Location finalDestination = destination;
                Optional<Edge> value = paths.stream().filter(i -> i.getDestination().equals(finalDestination)).findFirst();

                while (value.isPresent()) {
                    Location valueSource = value.get().getSource();
                    Location valueDestination = value.get().getDestination();
                    if (isPortal(valueSource, locationExpert) && isPortal(valueDestination, locationExpert)) {
                        // if is a portal, remove the internal path from portal source to sink.
                        // This is still required to be added as a link from the destination to source is needed.
                        result.removeLast();
                    }

                    destination = value.get().getSource();
                    Location finalDestination1 = destination;
                    value = paths.stream().filter(i -> i.getDestination().equals(finalDestination1)).findFirst();

                    result.add(destination);
                }
                Collections.reverse(result);
                result.remove(); // the first element is its current location

                return result.isEmpty() ? null : result; // if empty, find path is assumed to have failed. This is typically cased by errors in its arguments, not the algorithm itself

            } else {
                List<Location> unvisitedNeighbours =
                        IntStream.rangeClosed(0, 3)
                                .boxed()
                                .map(i -> vertex.getNeighbourLocation(90 * i))
                                .filter(i -> !locationIsVisited(i, visitedSet, indexConverter) && isValidLocation(i, locationExpert))
                                .sorted(Comparator.comparingInt(i -> new OneWayChecker(locationExpert).isOneWayAt(i, (int) vertex.getDirectionTo(i))))
                                .toList(); // find the next unvisited locations, sorted by preferring the path not being cornered

                for (var neighbour: unvisitedNeighbours) {
                    markLocationAsVisited(neighbour, visitedSet, indexConverter);
                    final var capturedNeighbour = neighbour; // explicit capture the location to be passed to closure
                    if (monsters != null && monsters.stream().map(Monster::getLocation).anyMatch(i -> i.getDistanceTo(capturedNeighbour) < 2)) {
                        // monster there, not through here!
                        continue;
                    }

                    ActorType neighbourType = locationExpert.getTypeAt(neighbour);
                    paths.add(new Edge(vertex, neighbour));

                    if (isPortal(neighbour, locationExpert)) {
                        // if is portal, register the path from the portal source to sink.
                        final var locations = portalLocations.get((CellType) neighbourType);
                        final var neighbourDestination = locations.get(0).equals(neighbour) ? locations.get(1) : locations.get(0);
                        paths.add(new Edge(neighbour, neighbourDestination));
                        neighbour = neighbourDestination; // then, add only enqueue the destination of the portal
                    }

                    queue.add(neighbour);
                }
            }
        }

        // if not required, failed.
        return null;
    }

    /**
     * Checks whether the location is a portal, according to the location expert.
     * @param loc: the location to be checked
     * @param expert: the information expert for the item locations
     * @return true if the location is a portal, false otherwise.
     */
    private boolean isPortal(Location loc, LocationExpert expert) {
        return expert.getTypeAt(loc) != null
                && expert.getTypeAt(loc) instanceof CellType
                && ((CellType) expert.getTypeAt(loc)).isPortal();
    }

    /**
     * Marks the location as visited.
     * @param location: location visited
     * @param visitedSet: HashSet of Integers representing the visited set of locations.
     * @param indexConverter: converter to convert the location to an integer index.
     */
    private void markLocationAsVisited(Location location, HashSet<Integer> visitedSet, LocationIndexConverter indexConverter) {
        visitedSet.add(indexConverter.getIndexByLocation(location));
    }

    /**
     * Checks if the location is visited.
     * @param location: location to be checked
     * @param visitedSet: HashSet of Integers representing the visited set of locations.
     * @param indexConverter: converter to convert the location to an integer index.
     * @return true if the location is visited, false otherwise.
     */
    private boolean locationIsVisited(Location location, HashSet<Integer> visitedSet, LocationIndexConverter indexConverter) {
        return visitedSet.contains(indexConverter.getIndexByLocation(location));
    }

    /**
     * Checks if the location is valid.
     * @param location: location to be checked
     * @param locationExpert: the information expert for the item locations
     * @return true if the location is valid (in bound and not a wall), false otherwise.
     */
    private boolean isValidLocation(Location location, LocationExpert locationExpert) {
        return locationExpert.isInBound(location) && !locationExpert.isWallAt(location);
    }

    /**
     * An edge with a source and a destination vertex,
     * where the vertex is represented by a location.
     */
    private class Edge {

        private final Location source;

        private final Location destination;

        /**
         * Creates an edge.
         *
         * @param source: the source vertex (location).
         * @param destination: the destination vertex (location).
         */
        public Edge(Location source, Location destination) {
            this.source = source;
            this.destination = destination;
        }

        /**
         * Gets the source vertex.
         * @return one vertex (location).
         */
        public Location getSource() {
            return source;
        }

        /**
         * Gets the destination vertex
         * @return The other vertex (location).
         */
        public Location getDestination() {
            return destination;
        }
    }
}
