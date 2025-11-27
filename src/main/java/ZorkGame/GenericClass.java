package ZorkGame;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericClass<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 12L;

    private List<T> items;

    public GenericClass() {
        this.items = new ArrayList<>();
    }

    public GenericClass(List<T> items) {
        this.items = new ArrayList<>(items);
    }

    public boolean add(T item) {
        if (item != null) {
            return items.add(item);
        }
        return false;
    }

    public void remove(T item) {
        items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    public T findByName(String name) {
        for (T item : items) {
            if (item instanceof Item i) {
                if (i.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            } else if (item instanceof NPC npc) {
                if (npc.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return null;
    }

    public T findByPartialName(String partialName) {
        for (T item : items) {
            if (item instanceof Item i) {
                if (i.getName().toLowerCase().contains(partialName.toLowerCase())) {
                    return item;
                }
            } else if (item instanceof NPC npc) {
                if (npc.getName().toLowerCase().contains(partialName.toLowerCase())) {
                    return item;
                }
            }
        }
        return null;
    }

    public <U> List<U> findAllByType(Class<U> type) {
        List<U> result = new ArrayList<>();
        for (T item : items) {
            if (type.isInstance(item)) {
                result.add(type.cast(item));
            }
        }
        return result;
    }

    public <U> U findByTypeAndName(Class<U> type, String name) {
        for (T item : items) {
            if (type.isInstance(item)) {
                U typedItem = type.cast(item);
                if (typedItem instanceof Item i) {
                    if (i.getName().equalsIgnoreCase(name)) {
                        return typedItem;
                    }
                } else if (typedItem instanceof NPC npc) {
                    if (npc.getName().equalsIgnoreCase(name)) {
                        return typedItem;
                    }
                }
            }
        }
        return null;
    }
}
