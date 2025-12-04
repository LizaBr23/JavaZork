package ZorkGame.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import ZorkGame.enums.Direction;
import ZorkGame.enums.AreaStorytelling;
import ZorkGame.utils.GenericClass;

public class Room implements Describable, Serializable {
    private final String DESCRIPTION;
    private final Map<Direction, Room> EXITS;
    private final GenericClass<Item> ITEMS = new GenericClass<>();
    private final GenericClass<NPC> NPCS = new GenericClass<>();



    public Room(String description) {
        this.DESCRIPTION = description;
        EXITS = new HashMap<>();
    }

    public void addItem(Item item){
        ITEMS.add(item);
    }

    public void removeItem(Item item){
        ITEMS.remove(item);
    }

    public void addNPC(NPC npc){
        NPCS.add(npc);
    }

    public void removeNPC(NPC npc){
        NPCS.remove(npc);
    }

    public List<Item> getItems() {
        return ITEMS.getAll();
    }

    public List<NPC> getNPCs() {
        return NPCS.getAll();
    }

    @Override
    public String getLongDescription(){
        StringBuilder itemList  = new StringBuilder();
        StringBuilder npcList = new StringBuilder();
        StringBuilder storytelling = new StringBuilder();

        try {
            String cleanedDescription = DESCRIPTION.toLowerCase();
            if (cleanedDescription.startsWith("in the ")) {
                cleanedDescription = cleanedDescription.substring(7);
            } else if (cleanedDescription.startsWith("in ")) {
                cleanedDescription = cleanedDescription.substring(3);
            }

            String roomNameUpper = cleanedDescription.toUpperCase().replace(" ", "");
            AreaStorytelling story = AreaStorytelling.valueOf(roomNameUpper);
            storytelling.append("\n").append(story.getDescription()).append("\n");
        } catch (IllegalArgumentException e) {
            //no story
        }

        if(!ITEMS.isEmpty()){
            itemList.append("\nYou see: \n");
            for (Item i : ITEMS.getAll()){
                itemList.append(i.getName()).append(" - ").append(i.getDescription()).append(" \n");
            }
        }
        if(!NPCS.isEmpty()){
            npcList.append("You see: \n");
            for (NPC n : NPCS.getAll()){
                npcList.append("  - ").append(n.getName()).append("\n");            }
        }
        return "You are " + DESCRIPTION + ".\nExits: " + getExitString() + storytelling + itemList + "\n" + npcList;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    public void setExit(Direction direction, Room neighbor) {
        EXITS.put(direction, neighbor);
    }

    public Room getExit(String directionStr) {
        Direction direction = Direction.fromString(directionStr);
        return direction != null ? EXITS.get(direction) : null;
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (Direction direction : EXITS.keySet()) {
            sb.append(direction.getDirectionName()).append(" ");
        }
        return sb.toString().trim();
    }


}
