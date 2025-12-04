package ZorkGame.threading;

import ZorkGame.models.NPC;
import ZorkGame.models.Room;
import java.util.List;
import java.util.Random;

public class NPCMovementThread extends Thread {
    private final NPC npc;
    private final List<Room> availableRooms;
    private final long movementIntervalMs;
    private volatile boolean running = true;
    private final Random random = new Random();

    public NPCMovementThread(NPC npc, List<Room> availableRooms, long movementIntervalMs) {
        this.npc = npc;
        this.availableRooms = availableRooms;
        this.movementIntervalMs = movementIntervalMs;
        this.setDaemon(true); // Thread will stop when main program exits
        this.setName("NPC-Movement-" + npc.getName());
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(movementIntervalMs);

                if (running && availableRooms.size() > 1) {
                    moveNPCToRandomRoom();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private synchronized void moveNPCToRandomRoom() {
        Room currentLocation = npc.getLocation();

        Room newRoom;
        do {
            newRoom = availableRooms.get(random.nextInt(availableRooms.size()));
        } while (newRoom == currentLocation && availableRooms.size() > 1);

        if (currentLocation != null) {
            currentLocation.removeNPC(npc);
        }
        newRoom.addNPC(npc);
        npc.setLocation(newRoom);

        System.out.println("\n[Background Activity] " + npc.getName() +
                          " has wandered to " + newRoom.getDescription() + ".");
    }

    public void stopMovement() {
        running = false;
        interrupt();
    }
}
