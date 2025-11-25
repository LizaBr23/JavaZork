package StartPackage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private String description;
    private Map<String, Room> exits;
    private List<Item> items = new ArrayList<>();
    private List<NPC> npcs = new ArrayList<>();



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
        return items;
    }

    public List<NPC> getNPCs() {
        return npcs;
    }

    public String getLongDescription(){
        StringBuilder itemList  = new StringBuilder();
        StringBuilder npcList = new StringBuilder();

        if(!items.isEmpty()){
            itemList.append("\nYou see: \n");
            for (Item i : items){
                itemList.append(i.getName()).append(" - ").append(i.getDescription()).append(" \n");
            }
        }
        if(!npcs.isEmpty()){
            npcList.append("You see: \n");
            for (NPC n : npcs){
                npcList.append("  - ").append(n.getName()).append("\n");            }
        }
        return "You are " + description + ".\nExits: " + getExitString() + "\n" + itemList.toString() + "\n" + npcList.toString();
    }



    public String getDescription() {
        return description;
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (String direction : exits.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString().trim();
    }


}
