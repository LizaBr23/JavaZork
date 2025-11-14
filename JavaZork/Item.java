import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private String name;
    private Room location;
    private int id;
    private boolean isVisible;

    public Item(String name, String description, Room location, int id) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.id = id;
        this.isVisible = true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getLocation() {
        return location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
