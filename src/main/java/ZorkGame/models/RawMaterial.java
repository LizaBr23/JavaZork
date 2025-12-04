package ZorkGame.models;

import java.io.Serial;
import java.io.Serializable;
public class RawMaterial extends Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    private final String RESULTING_INGREDIENT;

    public RawMaterial(String name, String description, Room location, int id, String resultingIngredient) {
        super(name, description, location, id);
        this.RESULTING_INGREDIENT = resultingIngredient;
    }


    public String getResultingIngredient() {
        return RESULTING_INGREDIENT;
    }

}

