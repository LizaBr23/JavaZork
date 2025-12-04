package ZorkGame.models;


import java.io.Serial;

public class Ingredient extends Item {
    @Serial
    private static final long serialVersionUID = 1L;

    public Ingredient(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }

}
