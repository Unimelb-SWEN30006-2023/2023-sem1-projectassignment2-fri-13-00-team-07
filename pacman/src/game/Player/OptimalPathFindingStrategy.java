package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
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
        final LinkedList<Edge> path = new LinkedList<>();
        LinkedList<Location> queue = new LinkedList<>();

        // enqueue the source
        queue.add(source);
        markLocationAsVisited(source, visitedSet, indexConverter);

        while (!queue.isEmpty()) {
            // while possible, dequeue
            Location vertex = queue.remove();

            if (predicate.satisfies(vertex, locationExpert)) { // this vertex is a valid destination
                LinkedList<Location> result = buildResultPath(vertex, path, locationExpert);
                // If empty, the path-finding is assumed to have failed.
                // This is typically cased by errors in its arguments, not the algorithm itself.
                return result.isEmpty() ? null : result;

            } else {
                // find the next unvisited locations,
                // sorted by preferring the path not being cornered
                List<Location> unvisitedNeighbours =
                        IntStream.rangeClosed(0, 3)
                                .boxed()
                                .map(i -> vertex.getNeighbourLocation(90 * i))
                                .filter(i -> !locationIsVisited(i, visitedSet, indexConverter) && isValidLocation(i, locationExpert))
                                .sorted(Comparator.comparingInt(i -> new OneWayChecker(locationExpert).isOneWayAt(i, (int) vertex.getDirectionTo(i))))
                                .toList();

                for (var neighbour: unvisitedNeighbours) {
                    markLocationAsVisited(neighbour, visitedSet, indexConverter);
                    // explicitly capture the location to be checked
                    final var capturedNeighbour = neighbour;
                    if (monsterNearBy(capturedNeighbour, monsters)) {
                        // monster there, move!
                        continue;
                    }

                    ActorType neighbourType = locationExpert.getTypeAt(neighbour);
                    path.add(new Edge(vertex, neighbour));

                    if (isPortal(neighbour, locationExpert)) {
                        // If is a portal, register the path from the portal source to the sink.
                        final var neighbourDestination = getPortalSource(portalLocations, neighbourType, neighbour);
                        path.add(new Edge(neighbour, neighbourDestination));
                        // Then, only enqueue the destination of the portal
                        neighbour = neighbourDestination;
                    }

                    queue.add(neighbour);
                }
            }
        }

        // Path not found
        return null;
    }

    /**
     * Builds a result path from the given path.
     * @param vertex: the destination vertex of the path - should be the foot of the path
     * @param path: a LinkedList of edges forming a path
     * @param locationExpert: The information expert for the game level's item locations.
     * @return a LinkedList of locations representing the result path.
     */
    private LinkedList<Location> buildResultPath(Location vertex, LinkedList<Edge> path, LocationExpert locationExpert) {
        LinkedList<Location> result = new LinkedList<>();

        result.add(vertex);
        Location destination = vertex;
        Location finalDestination = destination;
        // First find an edge to this destination
        Optional<Edge> edge = path.stream().filter(i -> i.getDestination().equals(finalDestination)).findFirst();

        while (edge.isPresent()) {
            Location edgeSource = edge.get().getSource();
            Location edgeDestination = edge.get().getDestination();
            if (isPortal(edgeSource, locationExpert) && isPortal(edgeDestination, locationExpert)) {
                // If the source is a portal, remove its 'internal' path from portal source to sink,
                // because the MovingActor will automatically jump to the partner location if it steps on a portal.
                // But still need to add this point for a complete path.
                result.removeLast();
            }

            destination = edge.get().getSource();
            Location finalDestination1 = destination;
            edge = path.stream().filter(i -> i.getDestination().equals(finalDestination1)).findFirst();

            result.add(destination);
        }
        Collections.reverse(result); // now the path is from source to destination
        result.remove(); // the first element is just the actor's current location

        return result;
    }

    /**
     * Gets the 'source' location in a portal pair (i.e. partner of the `otherPortal`).
     * @param portalLocations: a HashMap of CellType to Location pair,
     *                         recording the portal locations.
     * @param portalType: the type of this portal pair
     * @param otherPortal: the other portal in the pair
     * @return the location of 'this' portal.
     */
    private Location getPortalSource(HashMap<CellType, ArrayList<Location>> portalLocations, ActorType portalType, Location otherPortal) {
        final var locations = portalLocations.get((CellType) portalType);
        return locations.get(0).equals(otherPortal) ? locations.get(1) : locations.get(0); // the 'other' location is the source
    }

    /**
     * Checks if there are monsters close to the location (i.e. distance < 2).
     * @param loc: the location being checked.
     * @param monsters: an ArrayList of monsters.
     * @return true if the monsters are nearby, false otherwise.
     */
    private boolean monsterNearBy(Location loc, ArrayList<Monster> monsters) {
        return monsters != null && monsters.stream().map(Monster::getLocation).anyMatch(i -> i.getDistanceTo(loc) < 2);
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
