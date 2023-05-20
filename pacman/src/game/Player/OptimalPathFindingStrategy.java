package game.Player;

import ch.aplu.jgamegrid.Location;
import game.ActorType;
import game.Items.CellType;
import game.Items.LocationPredicate;
import game.Maps.PacManMap;
import game.Monsters.Monster;

import java.util.*;
import java.util.stream.IntStream;

public class OptimalPathFindingStrategy implements PathFindingStrategy {
    private final LinkedList<Edge> paths = new LinkedList<>();
    private final HashSet<Integer> visitedSet = new HashSet<>();
    private final HashMap<CellType, ArrayList<Location>> portalLocations = new HashMap<>();
    /**
     * Finds the optimal path to a sink that satisfies the `predicate` and avoids the monsters.
     *
     * @param source The source location.
     * @param predicate The predicate for a location to be considered a destination.
     * @param map The map on which the path is to be found.
     * @param monsters The monsters on the map. The monsters are avoided when finding the path.
     *
     * @return The optimal path, null on failure.
     */
    @Override
    public LinkedList<Location> findPath(Location source, LocationPredicate predicate, PacManMap map, ArrayList<Monster> monsters) {
        //System.out.println("The start location is " + source.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));

        for (int y = 0; y < map.getVerticalCellsCount(); y++) {
            for (int x = 0; x < map.getHorizontalCellsCount(); x++) {
                Location location = new Location(x, y);
                ActorType cellType = map.getTypeAt(location);

                if (cellType instanceof CellType && CellType.Portals().contains(cellType)) {
                    portalLocations.computeIfAbsent((CellType) cellType, k -> new ArrayList<>());
                    portalLocations.get(cellType).add(location);
                }
            }
        }

        LinkedList<Location> queue = new LinkedList<>();
        queue.add(source);
        markLocationAsVisited(source, map);

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
                    if (map.getTypeAt(value.get().getSource()) instanceof CellType && ((CellType) map.getTypeAt(value.get().getSource())).isPortal() && map.getTypeAt(value.get().getDestination()) instanceof CellType && ((CellType) map.getTypeAt(value.get().getDestination())).isPortal()) {
                        result.removeLast();
                    }

                    destination = value.get().getSource();
                    Location finalDestination1 = destination;
                    value = paths.stream().filter(i -> i.getDestination().equals(finalDestination1)).findFirst();

                    result.add(destination);
                }
                Collections.reverse(result);
                result.remove(); // the first element is its current location

//                for (final var v: result) {
//                    System.out.println("The path is through " + v.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
//                }

                return result;
            } else {

                List<Location> unvisitedNeighbours =
                        IntStream.rangeClosed(0, 3)
                                .boxed()
                                .map(i -> vertex.getNeighbourLocation(90 * i))
                                .filter(i -> !locationIsVisited(i, map) && isValidLocation(i, map))
                                .sorted(Comparator.comparingInt(i -> OneWayChecker.getInstance().isOneWayAt(i, map, (int) vertex.getDirectionTo(i))))
                                .toList();
                for (var neighbour: unvisitedNeighbours) {
                    markLocationAsVisited(neighbour, map);
                    final var capturedNeighbour = neighbour;
                    if (monsters != null && monsters.stream().map(Monster::getLocation).anyMatch(i -> i.getDistanceTo(capturedNeighbour) < 2)) {
                        // monster there, not through here!
                        continue;
                    }

                    //System.out.println("The neighbour location is " + neighbour.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
                    ActorType neighbourType = map.getTypeAt(neighbour);
                    paths.add(new Edge(vertex, neighbour));

                    if (neighbourType instanceof CellType && ((CellType) neighbourType).isPortal()) {
                        final var locations = portalLocations.get(neighbourType);
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

    private void markLocationAsVisited(Location location, PacManMap map) {
        visitedSet.add(location.y * map.getHorizontalCellsCount() + location.x);
    }

    private boolean locationIsVisited(Location location, PacManMap map) {
        return visitedSet.contains(location.y * map.getHorizontalCellsCount() + location.x);
    }

    private boolean isValidLocation(Location location, PacManMap map) {
        return map.isInBound(location) && !map.isWallAt(location);
    }
}
