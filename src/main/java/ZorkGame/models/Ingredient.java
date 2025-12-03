package ZorkGame.models;

import ZorkGame.enums.ItemCategory;

public class Ingredient extends Item {
    private static final long serialVersionUID = 1L;

    public Ingredient(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.INGREDIENT;
    }

    @Override
    public boolean canBeTaken() {
        return true;
    }

    @Override
    public void onPickup(Character player) {
    }
}
