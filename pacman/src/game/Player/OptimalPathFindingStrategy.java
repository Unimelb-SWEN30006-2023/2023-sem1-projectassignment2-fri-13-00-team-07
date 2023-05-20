package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
import game.Items.LocationPredicate;
import game.LocationExpert;
import game.LocationIndexConverter;
import game.Monsters.Monster;

import java.util.*;
import java.util.stream.IntStream;

public class OptimalPathFindingStrategy implements PathFindingStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedList<Location> findPath(Location source, LocationPredicate predicate, LocationExpert locationExpert, ArrayList<Monster> monsters) {
        //System.out.println("The start location is " + source.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
        LocationIndexConverter indexConverter = LocationIndexConverter.getInstance(locationExpert.getHorizontalCellsCount());

        final HashMap<CellType, ArrayList<Location>> portalLocations = locationExpert.getPortalLocations();
        final HashSet<Integer> visitedSet = new HashSet<>();
        final LinkedList<Edge> paths = new LinkedList<>();
        LinkedList<Location> queue = new LinkedList<>();

        queue.add(source);
        markLocationAsVisited(source, visitedSet, indexConverter);

        while (!queue.isEmpty()) {
            Location vertex = queue.remove();

//            System.out.println("The vertex location is " + vertex.getNeighbourLocation(Location.CompassDirection.SOUTHEAST) + " type is " + getTypeAt(vertex) + " statisfies: " + predicate.satisfies(vertex, locationExpert) + " ?? " + (locationExpert.getTypeAt(vertex).equals(CellType.PILL)));

            if (predicate.satisfies(vertex, locationExpert)) {
                //System.out.println("The end location is " + vertex.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
                LinkedList<Location> result = new LinkedList<>();

                result.add(vertex);
                Location destination = vertex;
                Location finalDestination = destination;
                Optional<Edge> value = paths.stream().filter(i -> i.getDestination().equals(finalDestination)).findFirst();

                while (value.isPresent()) {
                    Location valueSource = value.get().getSource();
                    Location valueDestination = value.get().getDestination();
                    if (isPortal(valueSource, locationExpert) && isPortal(valueDestination, locationExpert)) {
                        result.removeLast();
                    }

                    destination = value.get().getSource();
                    Location finalDestination1 = destination;
                    value = paths.stream().filter(i -> i.getDestination().equals(finalDestination1)).findFirst();

                    result.add(destination);
                }
                Collections.reverse(result);
                result.remove(); // the first element is its current location

                return result.isEmpty() ? null : result;

            } else {

                List<Location> unvisitedNeighbours =
                        IntStream.rangeClosed(0, 3)
                                .boxed()
                                .map(i -> vertex.getNeighbourLocation(90 * i))
                                .filter(i -> !locationIsVisited(i, visitedSet, indexConverter) && isValidLocation(i, locationExpert))
                                .sorted(Comparator.comparingInt(i -> new OneWayChecker(locationExpert).isOneWayAt(i, (int) vertex.getDirectionTo(i))))
                                .toList();
                for (var neighbour: unvisitedNeighbours) {
                    markLocationAsVisited(neighbour, visitedSet, indexConverter);
                    final var capturedNeighbour = neighbour;
                    if (monsters != null && monsters.stream().map(Monster::getLocation).anyMatch(i -> i.getDistanceTo(capturedNeighbour) < 2)) {
                        // monster there, not through here!
                        continue;
                    }

//                    System.out.println("The neighbour location is " + neighbour.getNeighbourLocation(Location.CompassDirection.SOUTHEAST) + " type is " + getTypeAt(neighbour));
                    ActorType neighbourType = locationExpert.getTypeAt(neighbour);
                    paths.add(new Edge(vertex, neighbour));

                    if (isPortal(neighbour, locationExpert)) {
                        final var locations = portalLocations.get((CellType) neighbourType);
                        final var neighbourDestination = locations.get(0).equals(neighbour) ? locations.get(1) : locations.get(0);
                        paths.add(new Edge(neighbour, neighbourDestination));
                        neighbour = neighbourDestination;
                    }

                    queue.add(neighbour);
                }
            }
        }

        //System.out.println("bfs did not find anything");

        return null;
    }

    /**
     * Checks whether the location is a portal, according to the location expert.
     * @param loc: the location to be checked
     * @param expert: the location expert
     * @return true if the location is a portal, false otherwise.
     */
    private boolean isPortal(Location loc, LocationExpert expert) {
        return expert.getTypeAt(loc) != null
                && expert.getTypeAt(loc) instanceof CellType
                && ((CellType) expert.getTypeAt(loc)).isPortal();
    }

    private void markLocationAsVisited(Location location, HashSet<Integer> visitedSet, LocationIndexConverter indexConverter) {
        visitedSet.add(indexConverter.getIndexByLocation(location));
    }

    private boolean locationIsVisited(Location location, HashSet<Integer> visitedSet, LocationIndexConverter indexConverter) {
        return visitedSet.contains(indexConverter.getIndexByLocation(location));
    }

    private boolean isValidLocation(Location location, LocationExpert locationExpert) {
        return locationExpert.isInBound(location) && !locationExpert.isWallAt(location);
    }
}
