package ZorkGame.models;

import java.io.Serializable;
import ZorkGame.enums.ItemCategory;

public abstract class Item implements GameEntity, Locatable, Describable, Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private String name;
    private Room location;
    private int id;

    public Item(String name, String description, Room location, int id) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLongDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Room getLocation() {
        return location;
    }

    @Override
    public void setLocation(Room location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public abstract ItemCategory getCategory();
    public abstract boolean canBeTaken();
    public abstract void onPickup(Character player);
}
