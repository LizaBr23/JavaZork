import java.util.*;

public class GameMap {
    private Room[][] map2D;
    private Set<Room> visitedRooms;

    public GameMap(List<Room> rooms){
        this.visitedRooms = new HashSet<>();
        this.map2D = new Room[4][4];
        fillMap(rooms);
    }

    private void fillMap(List<Room> rooms){
        int index = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                map2D[i][j] = rooms.get(index);
                index++;
            }
        }
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

        System.out.println("\n MAP");

        for (int row = 0; row < map2D.length; row++) {
            printVerticalConnections(row);
            printRoomsAndHorizontalConnections(row);
        }
    }

    private void printVerticalConnections(int row) {
        if (row == 0) return;

        for (int col = 0; col < map2D[row].length; col++) {
            Room currentRoom = map2D[row][col];
            Room roomAbove = map2D[row - 1][col];

            if (currentRoom != null && roomAbove != null &&
                    isVisited(currentRoom) && isVisited(roomAbove) &&
                    areConnected(roomAbove, currentRoom)) {

                System.out.print("        |        ");
            } else {
                System.out.print("                 ");
            }
        }
        System.out.println();
    }


    private void printRoomsAndHorizontalConnections(int row) {
        for (int col = 0; col < map2D[row].length; col++) {
            Room room = map2D[row][col];

            if (room != null && isVisited(room)) {
                String roomName = getRoomName(room);
                System.out.print(" " + roomName + " ");
            } else {
                System.out.print("                 ");
            }

            if (col < map2D[row].length - 1) {
                Room nextRoom = map2D[row][col + 1];

                if (room != null && nextRoom != null &&
                        isVisited(room) && isVisited(nextRoom) &&
                        areConnected(room, nextRoom)) {

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



}
