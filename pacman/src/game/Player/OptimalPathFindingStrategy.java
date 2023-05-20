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
    private final LinkedList<Edge> paths = new LinkedList<>();
    private final HashSet<Integer> visitedSet = new HashSet<>();
    private final HashMap<CellType, ArrayList<Location>> portalLocations = new HashMap<>();
    private LocationExpert locationExpert;
    private LocationIndexConverter indexConverter;
    /**
     * Finds the optimal path to a sink that satisfies the `predicate` and avoids the monsters.
     *
     * @param source The source location.
     * @param predicate The predicate for a location to be considered a destination.
     * @param monsters The monsters on the map. The monsters are avoided when finding the path.
     *
     * @return The optimal path, null on failure.
     */
    @Override
    public LinkedList<Location> findPath(Location source, LocationPredicate predicate, LocationExpert locationExpert, ArrayList<Monster> monsters) {
        //System.out.println("The start location is " + source.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
        this.locationExpert = locationExpert;
        this.indexConverter = LocationIndexConverter.getInstance(locationExpert.getHorizontalCellsCount());
        setUpPortalLocations();

        LinkedList<Location> queue = new LinkedList<>();
        queue.add(source);
        markLocationAsVisited(source);

        while (!queue.isEmpty()) {
            Location vertex = queue.remove();

            if (predicate.satisfies(vertex)) {
                //System.out.println("The end location is " + vertex.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
                LinkedList<Location> result = new LinkedList<>();

                result.add(vertex);
                Location destination = vertex;
                Location finalDestination = destination;
                Optional<Edge> value = paths.stream().filter(i -> i.getDestination().equals(finalDestination)).findFirst();

                while (value.isPresent()) {
                    Location valueSource = value.get().getSource();
                    Location valueDestination = value.get().getDestination();
                    if (isPortal(valueSource) && isPortal(valueDestination)) {
                        result.removeLast();
                    }

                    destination = value.get().getSource();
                    Location finalDestination1 = destination;
                    value = paths.stream().filter(i -> i.getDestination().equals(finalDestination1)).findFirst();

                    result.add(destination);
                }
                Collections.reverse(result);
                result.remove(); // the first element is its current location

                return result;
            } else {

                List<Location> unvisitedNeighbours =
                        IntStream.rangeClosed(0, 3)
                                .boxed()
                                .map(i -> vertex.getNeighbourLocation(90 * i))
                                .filter(i -> !locationIsVisited(i) && isValidLocation(i))
                                .sorted(Comparator.comparingInt(i -> new OneWayChecker(locationExpert).isOneWayAt(i, (int) vertex.getDirectionTo(i))))
                                .toList();
                for (var neighbour: unvisitedNeighbours) {
                    markLocationAsVisited(neighbour);
                    final var capturedNeighbour = neighbour;
                    if (monsters != null && monsters.stream().map(Monster::getLocation).anyMatch(i -> i.getDistanceTo(capturedNeighbour) < 2)) {
                        // monster there, not through here!
                        continue;
                    }

                    //System.out.println("The neighbour location is " + neighbour.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
                    ActorType neighbourType = getTypeAt(neighbour);
                    paths.add(new Edge(vertex, neighbour));

                    if (isPortal(neighbour)) {
                        final var locations = portalLocations.get((CellType) neighbourType);
                        final var neighbourDestination = locations.get(0).equals(neighbour) ? locations.get(1) : locations.get(0);
                        paths.add(new Edge(neighbour, neighbourDestination));
                        neighbour = neighbourDestination;
                    }

                    queue.add(neighbour);
                }
            }
        }

        return null;
    }

    private void setUpPortalLocations() {
        for (int y = 0; y < locationExpert.getVerticalCellsCount(); y++) {
            for (int x = 0; x < locationExpert.getHorizontalCellsCount(); x++) {
                Location location = new Location(x, y);
                ActorType cellType = getTypeAt(location);

                if (CellType.Portals().contains((CellType) cellType)) {
                    portalLocations.computeIfAbsent((CellType) cellType, k -> new ArrayList<>());
                    portalLocations.get(cellType).add(location);
                }
            }
        }
    }

    private ActorType getTypeAt(Location loc) {
        return locationExpert.getTypeAt(loc);
    }

    private boolean isPortal(Location loc) {
        return getTypeAt(loc) != null && ((CellType) getTypeAt(loc)).isPortal();
    }

    private void markLocationAsVisited(Location location) {
        visitedSet.add(indexConverter.getIndexByLocation(location));
    }

    private boolean locationIsVisited(Location location) {
        return visitedSet.contains(indexConverter.getIndexByLocation(location));
    }

    private boolean isValidLocation(Location location) {
        return locationExpert.isInBound(location) && !locationExpert.isWallAt(location);
    }
}
