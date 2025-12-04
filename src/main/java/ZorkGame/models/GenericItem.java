package ZorkGame.models;

import java.io.Serial;

public class GenericItem extends Item {
    @Serial
    private static final long serialVersionUID = 1L;

    public GenericItem(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }

}
