import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface Convertible {
    boolean canUseOn(String rawMaterialName);
    String getResultIngredient(String rawMaterialName);
}

public abstract class Tool extends Item implements Serializable, Convertible {
    private static final long serialVersionUID = 4L;
    List<String> usableOn  = new ArrayList<>();

    public Tool(String name, String description, Room location, int id) {
        super(name, description, location, id);
    }

    public boolean canUseOn(String rawMaterialName) {
        for (String material : usableOn) {
            if (material.equalsIgnoreCase(rawMaterialName)) {
                return true;
            }
        }
        return false;
    }

    public String getResultIngredient(String rawMaterialName) {
        Map<String, String> conversions = getConversionMap();

        for (String key : conversions.keySet()) {
            if (key.equalsIgnoreCase(rawMaterialName)) {
                return conversions.get(key);
            }
        }
        return null;
    }

    protected abstract Map<String, String> getConversionMap();

    public abstract List<String> getUsableOn();
}

class Shovel extends Tool {
    private static final long serialVersionUID = 7L;

    public Shovel(String name, String description, Room location, int id, List<String> usableOn) {
        super(name, description, location, id);
        this.usableOn = usableOn;
    }

    @Override
    protected Map<String, String> getConversionMap() {
        Map<String, String> map = new HashMap<>();
        map.put("ashRoot", "ashRootSpice");
        return map;
    }

    @Override
    public List<String> getUsableOn() {
        return usableOn;
    }
}

class Sandpaper extends Tool {
    private static final long serialVersionUID = 9L;

    public Sandpaper(String name, String description, Room location, int id, List<String> usableOn) {
        super(name, description, location, id);
        this.usableOn = usableOn;
    }

    @Override
    protected Map<String, String> getConversionMap() {
        Map<String, String> map = new HashMap<>();
        map.put("sharpThorn", "thornPowder");
        map.put("darkStone", "stoneDust");
        return map;
    }

    @Override
    public List<String> getUsableOn() {
        return usableOn;
    }
}

class Knife extends Tool {
    private static final long serialVersionUID = 6L;

    public Knife(String name, String description, Room location, int id, List<String> usableOn) {
        super(name, description, location, id);
        this.usableOn = usableOn;
    }

    @Override
    protected Map<String, String> getConversionMap() {
        Map<String, String> map = new HashMap<>();
        map.put("lavenderBush", "lavender");
        return map;
    }

    @Override
    public List<String> getUsableOn() {
        return usableOn;
    }
}

class Pickaxe extends Tool{
    private static final long serialVersionUID = 5L;

    public Pickaxe(String name, String description, Room location, int id, List<String> usableOn) {
        super(name, description, location, id);
        this.usableOn = usableOn;
    }

    @Override
    protected Map<String, String> getConversionMap() {
        Map<String, String> map = new HashMap<>();
        map.put("saltStone", "saltCrystal");
        map.put("shinyNut", "nutShell");
        return map;
    }

    @Override
    public List<String> getUsableOn() {
        return usableOn;
    }
}

class Secateurs extends Tool {
    private static final long serialVersionUID = 8L;

    public Secateurs(String name, String description, Room location, int id, List<String> usableOn) {
        super(name, description, location, id);
        this.usableOn = usableOn;
    }

    @Override
    protected Map<String, String> getConversionMap() {
        Map<String, String> map = new HashMap<>();
        map.put("fern", "fernTips");
        map.put("messyGrass", "sweetGrass");
        return map;
    }

    @Override
    public List<String> getUsableOn() {
        return usableOn;
    }
}