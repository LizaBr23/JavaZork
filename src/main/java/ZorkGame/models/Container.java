package ZorkGame.models;

import java.util.List;

public interface Container<T> {
    void add(T item);
    void remove(T item);
    List<T> getAll();
    boolean isEmpty();
}
