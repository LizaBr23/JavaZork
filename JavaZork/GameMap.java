import javax.swing.text.Position;
import java.io.Serializable;
import java.util.*;

public class GameMap implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Room, Position> roomPositions;
    private Set<Room> visitedRooms;

    public GameMap() {
        this.visitedRooms = new HashSet<>();
        this.roomPositions = new HashMap<>();
    }

    public void visitRoom(Room room) {
        if (room != null) {
            visitedRooms.add(room);
        }
    }

    public boolean isVisited(Room room) {
        return visitedRooms.contains(room);
    }

    public void showMap(Room currentRoom) {
        if (visitedRooms.isEmpty()) {
            System.out.println("You haven't go anywhere, use go command to move.");
            return;
        }
        Room[][] map2D = buildMapArray(currentRoom);

        System.out.println("\n------------MAP-----------");
        System.out.println("Your location: [Room Name]\n");

        for (int row = 0; row < map2D.length; row++) {
            printVerticalConnections(map2D, row, currentRoom);
            printRoomsAndHorizontalConnections(map2D, row, currentRoom);
        }
    }

    private Room[][] buildMapArray(Room startRoom) {
        roomPositions.clear();

        Set<Room> exploredInDFS = new HashSet<>();
        calculateRelativePositions(startRoom, exploredInDFS, 0, 0);

        makePositionsAbsolute();

        int[] size = getMapSize();
        int width = size[0];
        int height = size[1];

        Room[][] map = new Room[height][width];
        for (Room room : roomPositions.keySet()) {
            if (visitedRooms.contains(room)) {
                Position pos = roomPositions.get(room);
                map[pos.y][pos.x] = room;
            }
        }

        return map;

    }

    private void calculateRelativePositions(Room current, Set<Room> visited, int x, int y) {
        if (current == null || visited.contains(current)) {
            return;
        }
        roomPositions.put(current, new Position(x, y));
        visited.add(current);

        Room west = current.getExit("west");
        Room east = current.getExit("east");
        Room north = current.getExit("north");
        Room south = current.getExit("south");

        if (west != null && !visited.contains(west)) {
            calculateRelativePositions(west, visited, x - 1, y);
        }
        if (east != null && !visited.contains(east)) {
            calculateRelativePositions(east, visited, x + 1, y);
        }
        if (north != null && !visited.contains(north)) {
            calculateRelativePositions(north, visited, x, y - 1);
        }
        if (south != null && !visited.contains(south)) {
            calculateRelativePositions(south, visited, x, y + 1);
        }
    }

    private void makePositionsAbsolute() {

        int minX = 0;
        int minY = 0;

        for (Position pos : roomPositions.values()) {
            minX = Math.min(minX, pos.x);
            minY = Math.min(minY, pos.y);
        }

        for (Position pos : roomPositions.values()) {
            pos.x -= minX;
            pos.y -= minY;
        }

    }

    private int[] getMapSize() {
        int maxX = 0;
        int maxY = 0;

        for (Position pos : roomPositions.values()) {
            maxX = Math.max(maxX, pos.x);
            maxY = Math.max(maxY, pos.y);
        }
        return new int[]{maxX + 1, maxY + 1};
    }

    private void printVerticalConnections(Room[][] map, int row, Room currentRoom) {
        if (row == 0) return;

        for (int col = 0; col < map[row].length; col++) {
            Room current = map[row][col];
            Room above = map[row - 1][col];

            if (current != null && above != null &&
                    isVisited(current) && isVisited(above) &&
                    areConnected(above, current)) {

                System.out.print("        |        ");
            } else {
                System.out.print("                 ");
            }
        }
        System.out.println();
    }


    private void printRoomsAndHorizontalConnections(Room[][] map, int row, Room currentRoom) {
        for (int col = 0; col < map[row].length; col++) {
            Room room = map[row][col];

            if (room != null && isVisited(room)) {
                String roomName = getRoomName(room);
                boolean isCurrent = room.equals(currentRoom);
                if (isCurrent) {
                    System.out.print("[" + roomName + "]");
                } else {
                    System.out.print(" " + roomName + " ");
                }
            } else {
                System.out.print("                 ");
            }

            if (col < map[row].length - 1) {
                Room next = map[row][col + 1];

                if (room != null && next != null &&
                        isVisited(room) && isVisited(next) &&
                        areConnected(room, next)) {

                    System.out.print("-");
                } else {
                    System.out.print(" ");
                }
            }
        }
        System.out.println();
    }

    private boolean areConnected(Room room1, Room room2) {
        if (room1 == null || room2 == null) return false;

        return room1.getExit("north") == room2 ||
                room1.getExit("south") == room2 ||
                room1.getExit("east") == room2 ||
                room1.getExit("west") == room2;
    }

    private String getRoomName(Room room) {
        String name = room.getDescription();

        if (name.startsWith("in the ")) {
            name = name.substring(7);
        }

        if (name.length() > 0) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        if (name.length() > 15) {
            return name.substring(0, 15);
        }

        while (name.length() < 15) {
            name += " ";
        }
        return name;
    }

    private static class Position implements Serializable {
        private static final long serialVersionUID = 1L;
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
