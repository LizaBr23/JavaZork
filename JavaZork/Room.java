import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private String description;
    private Map<String, Room> exits;
    private List<Item> items = new ArrayList<>();


    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
    }

    public void addItem(Item item){
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public String getLongDescription(){
        StringBuilder itemList  = new StringBuilder();
        if(!items.isEmpty()){
            itemList.append("\nYou see: ");
            for (Item i : items){
                itemList.append(i.getName()).append(" - ").append(i.getDescription()).append(" \n");
            }
        }
        return "You are " + description + ".\nExits: " + getExitString() + "\n" + itemList.toString();
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
