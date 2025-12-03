package ZorkGame.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import ZorkGame.dialogue.DialogueHandler;

public class NPC implements GameEntity, Locatable, Serializable {
    @Serial
    private static final long serialVersionUID = 11L;

    protected String name;
    protected Room location;
    protected Map<String, Integer> inventory;
    protected boolean isTalking;
    protected DialogueHandler dialogueHandler;
    protected String itemWantedToTalk;


    public NPC(String name, Room location, DialogueHandler handler, String itemWantedToTalk) {
        this.name = name;
        this.location = location;
        this.inventory = new HashMap<>();
        this.isTalking = false;
        this.dialogueHandler = handler;
        this.itemWantedToTalk = itemWantedToTalk;
    }

    public String getItemWantedToTalk(){
        return itemWantedToTalk;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Room getLocation() {
        return location;
    }

    @Override
    public void setLocation(Room location) {
        this.location = location;
    }

    @Override
    public String getDescription() {
        return getDescription(false);
    }

    public void addToInventory(String itemName, int price) {
        inventory.put(itemName, price);
    }

    public boolean hasItem(String itemName) {
        return inventory.containsKey(itemName);
    }

    public int getItemPrice(String itemName) {
        return inventory.getOrDefault(itemName, -1);
    }

    public Set<String> getAvailableItems() {
        return inventory.keySet();
    }

    public void removeFromInventory(String itemName) {
        inventory.remove(itemName);
    }

    public void setTalking(boolean talking) {
        this.isTalking = talking;
    }

    public boolean isTalking() {
        return isTalking;
    }

    public List<String> getDialogueOptions(boolean hasObject) {
        return dialogueHandler.getOptions(hasObject);
    }

    public String getDialogueResponse(int choice, boolean hasObject) {
        return dialogueHandler.getResponse(choice, hasObject);
    }

    public String getDescription(boolean hasObject) {
        return dialogueHandler.getDescription(hasObject);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}