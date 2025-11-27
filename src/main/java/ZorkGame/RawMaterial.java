package ZorkGame;

import java.io.Serializable;

public class RawMaterial extends Item implements Serializable{
    private static final long serialVersionUID = 10L;

    private String requiredTool;
    private String resultingIngredient;

    public RawMaterial(String name, String description, Room location, int id, String requiredTool, String resultingIngredient) {
        super(name, description, location, id);
        this.requiredTool = requiredTool;
        this.resultingIngredient = resultingIngredient;
    }

    public String getRequiredTool() {
        return requiredTool;
    }

    public String getResultingIngredient() {
        return resultingIngredient;
    }
}

