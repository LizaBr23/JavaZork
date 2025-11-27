package ZorkGame;

// Interface for Tool items that can convert raw materials into ingredients
public interface Convertible {

    boolean canUseOn(String rawMaterialName);
    String getResultIngredient(String rawMaterialName);

}
