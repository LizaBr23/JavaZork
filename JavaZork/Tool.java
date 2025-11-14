import java.util.ArrayList;
import java.util.List;

public class Tool extends Item{
    List<String> usedOn = new ArrayList<>();
    public Tool(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }


}

class Shovel extends Tool{

    public Shovel(String name, String description, Room location, int id, List<String> useFor) {
        super(name, description, location, id);
        this.usedOn = useFor;
    }
}

class Sandpaper extends Tool{

    public Sandpaper(String name, String description, Room location, int id, List<String> useFor) {
        super(name, description, location, id);
        usedOn = useFor;
    }
}

class Knife extends Tool{

    public Knife(String name, String description, Room location, int id, List<String> useFor) {
        super(name, description, location, id);
        usedOn = useFor;
    }
}

class Pickaxe extends Tool{

    public Pickaxe(String name, String description, Room location, int id, List<String> useFor) {
        super(name, description, location, id);
        usedOn = useFor;
    }
}

class Secateurs extends Tool{

    public Secateurs(String name, String description, Room location, int id, List<String> useFor) {
        super(name, description, location, id);
        usedOn = useFor;
    }
}