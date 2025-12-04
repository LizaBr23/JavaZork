package ZorkGame.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;


abstract class AbstractTool extends Item implements Convertible, Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    protected Map<String, String> conversionMap;

    public AbstractTool(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }

    @Override
    public boolean canUseOn(String rawMaterialName) {
        for (String material : conversionMap.keySet()) {
            if (material.equalsIgnoreCase(rawMaterialName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getResultIngredient(String rawMaterialName) {
        for (Map.Entry<String, String> entry : conversionMap.entrySet()) {
            String rawMaterial = entry.getKey();
            String resultIngredient = entry.getValue();

            if (rawMaterial.equalsIgnoreCase(rawMaterialName)) {
                return resultIngredient;
            }
        }
        return null;
    }

}

public class Tool extends AbstractTool {
    @Serial
    private static final long serialVersionUID = 11L;

    public Tool(String name, String description, Room location, int id,
                Map<String, String> conversionMap) {
        super(name, description, location, id);
        this.conversionMap = conversionMap;
    }
}
