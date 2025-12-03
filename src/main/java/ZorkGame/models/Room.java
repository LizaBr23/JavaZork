package ZorkGame.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import ZorkGame.enums.Direction;
import ZorkGame.enums.AreaStorytelling;
import ZorkGame.utils.GenericClass;

public class Room implements Describable, Serializable {
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

    public void removeItem(Item item){
        items.remove(item);
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

    @Override
    public String getLongDescription(){
        StringBuilder itemList  = new StringBuilder();
        StringBuilder npcList = new StringBuilder();
        StringBuilder storytelling = new StringBuilder();

        try {
            String cleanedDescription = description.toLowerCase();
            if (cleanedDescription.startsWith("in the ")) {
                cleanedDescription = cleanedDescription.substring(7);
            } else if (cleanedDescription.startsWith("in ")) {
                cleanedDescription = cleanedDescription.substring(3);
            }

            String roomNameUpper = cleanedDescription.toUpperCase().replace(" ", "");
            AreaStorytelling story = AreaStorytelling.valueOf(roomNameUpper);
            if (story != null) {
                storytelling.append("\n").append(story.getDescription()).append("\n");
            }
        } catch (IllegalArgumentException e) {
            //no story
        }

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
        return "You are " + description + ".\nExits: " + getExitString() + storytelling.toString() + itemList.toString() + "\n" + npcList.toString();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setExit(Direction direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    public Room getExit(String directionStr) {
        Direction direction = Direction.fromString(directionStr);
        return direction != null ? exits.get(direction) : null;
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (Direction direction : exits.keySet()) {
            sb.append(direction.getDirectionName()).append(" ");
        }
        return sb.toString().trim();
    }


}
