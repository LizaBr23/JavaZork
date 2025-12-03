package ZorkGame.models;

import ZorkGame.enums.ItemCategory;

public class GenericItem extends Item {
    private static final long serialVersionUID = 1L;

    public GenericItem(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.QUEST_ITEM;
    }

    @Override
    public boolean canBeTaken() {
        return true;
    }

    @Override
    public void onPickup(Character player) {
    }
}
