package ZorkGame.models;

import java.io.Serializable;
import ZorkGame.enums.ItemCategory;

public class RawMaterial extends Item implements Serializable {
    private static final long serialVersionUID = 10L;

    private String requiredTool;
    private String resultingIngredient;

    public RawMaterial(String name, String description, Room location, int id, String requiredTool, String resultingIngredient) {
        super(name, description, location, id);
        this.requiredTool = requiredTool;
        this.resultingIngredient = resultingIngredient;
    }


    public String getResultingIngredient() {
        return resultingIngredient;
    }

    @Override
    public ItemCategory getCategory() {
        return ItemCategory.RAW_MATERIAL;
    }

    @Override
    public boolean canBeTaken() {
        return false;
    }

    @Override
    public void onPickup(Character player) {
    }
}

