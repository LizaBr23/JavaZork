import java.util.HashMap;
import java.util.Map;

public abstract class NPC {
    protected final String NAME;
    protected Room location;
    protected boolean isTalking;
    protected String description;
    protected Map<String, String> sales;

    public NPC(String name, Room location) {
        this.NAME = name;
        this.location = location;
    }

    public abstract void setSales(Map<String, String> sales);
    public abstract void changeTalking(boolean talking);
    public abstract String getName();
    public abstract String getDescription();
}

class Bob extends NPC {
    public Bob(String name, Room location) {
        super(name, location);
        isTalking = false;
        description = "Bob the bob\nOld angry man sitting in the pub with no money craving for a beer. Some people say he sells good quality tools which last for decades. However many are afraid of talking to him.";
        sales = new HashMap<>();
    }

    @Override
    public void setSales(Map<String, String> sales) {
        sales.put("knife","");
        sales.put("pickaxe","");
        sales.put("shovel","");
        sales.put("secateurs","");
        sales.put("sandpaper","");
    }

    @Override
    public void changeTalking(boolean talking) {
        isTalking = true;
    }

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public String getDescription() {
        if(isTalking) {
            return description + "Bob is smiling at you";
        }
        return description + "\nBob doesn't notice you if you have no alcohol with you";
    }
}
