package ZorkGame;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    public boolean remove(T item) {
        return items.remove(item);
    }

    public T removeAt(int index) {
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }
        return null;
    }

    public T get(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public T find(Predicate<T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public List<T> findAll(Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    @Override
    public String toString() {
        return "GenericClass{" +
                "size=" + items.size() +
                ", items=" + items +
                '}';
    }
}
