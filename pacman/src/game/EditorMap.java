package game;

import ch.aplu.jgamegrid.Location;
import game.Items.CellType;

import java.util.*;


public class EditorMap implements PacManMap {
    private ActorType[][] map;
    private String name; //Ziming: I want a name here

    public EditorMap(ActorType[][] map) {
        this.map = map;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public ActorType getTypeAt(Location loc) {
        return map[loc.x][loc.y];
    }

    public boolean isCellType(Location loc) {
        return map[loc.x][loc.y] instanceof CellType;
    }

    public boolean isCharacterType(Location loc){
        return map[loc.x][loc.y] instanceof CharacterType;
    }

    public int getNumRows() {
        return map.length;
    }

    public int getNumCols() {
        return map[0].length;
    }

    /**
     * check if we can reach a cell from a start location, only consider walls as obstacles
     * @param from start location
     * @param to to location
     * @return true if accessible, false if not
     */
    public boolean canReach(Location from, Location to){
        return MovingActor.findOptimalPath(from, to, this) != null;
    }

//    public Location BFSNextCellToGo(Location from, Location to){
//            int[] start = new int[]{from.y, from.x};
//            int[] des = new int[]{to.y, to.x};
//            int m = map.length;
//            int n = map[0].length;
//            boolean[][] visited = new boolean[m][n];
//            int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
//            Queue<int[]> queue = new LinkedList<>();
//            queue.offer(start);
//            visited[start[0]][start[1]] = true;
//            Map<int[], int[]> parentMap = new HashMap<>();
//            while (!queue.isEmpty()) {
//                int[] curr = queue.poll();
//                if (curr[0] == des[0] && curr[1] == des[1]) {
//                    // Destination is reached, construct the path and return the first cell
//                    int[] pathCurr = curr;
//                    while (!Arrays.equals(pathCurr, start)) {
//                        int[] parent = parentMap.get(pathCurr);
//                        pathCurr = parent;
//                    }
//                    // Construct the new Location object with the first cell's coordinates and return it
//                    return new Location(pathCurr[1], pathCurr[0]);
//                }
//                // Explore the neighbors
//                for (int[] dir : directions) {
//                    int nextX = curr[0] + dir[0];
//                    int nextY = curr[1] + dir[1];
//                    Location loc = new Location(nextX, nextY);
//                    if (nextX >= 0 && nextX < m
//                            && nextY >= 0 && nextY < n
//                            && (isCellType(loc) && ((CellType)getTypeAt(new Location(nextX, nextY))).getCellChar() != 'x')
//                            && !visited[nextX][nextY]) {
//                        queue.offer(new int[]{nextX, nextY});
//                        visited[nextX][nextY] = true;
//                        // Map the visited cell to its parent cell
//                        parentMap.put(new int[]{nextX, nextY}, curr);
//                    }
//                }
//            }
//            // No path found
//            return null;
//        }
}
