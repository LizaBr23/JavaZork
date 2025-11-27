package ZorkGame;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Room implements Serializable {
    private String description;
    private Map<Direction, Room> exits;
    private GenericClass<Item> items = new GenericClass<>();
    private GenericClass<NPC> npcs = new GenericClass<>();



    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void addNPC(NPC npc){
        npcs.add(npc);
    }

    public List<Item> getItems() {
        return items.getAll();
    }

    public List<NPC> getNPCs() {
        return npcs.getAll();
    }

    public String getLongDescription(){
        StringBuilder itemList  = new StringBuilder();
        StringBuilder npcList = new StringBuilder();

        if(!items.isEmpty()){
            itemList.append("\nYou see: \n");
            for (Item i : items.getAll()){
                itemList.append(i.getName()).append(" - ").append(i.getDescription()).append(" \n");
            }
        }
        if(!npcs.isEmpty()){
            npcList.append("You see: \n");
            for (NPC n : npcs.getAll()){
                npcList.append("  - ").append(n.getName()).append("\n");            }
        }
        return "You are " + description + ".\nExits: " + getExitString() + "\n" + itemList.toString() + "\n" + npcList.toString();
    }



    public String getDescription() {
        return description;
    }

    // Set exit using Direction enum
    public void setExit(Direction direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    // Get exit room using Direction enum
    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    // Get exit room using string (converts to enum)
    public Room getExit(String directionStr) {
        Direction direction = Direction.fromString(directionStr);
        return direction != null ? exits.get(direction) : null;
    }

    // Get all exits as a formatted string
    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (Direction direction : exits.keySet()) {
            sb.append(direction.getDirectionName()).append(" ");
        }
        return sb.toString().trim();
    }


}
