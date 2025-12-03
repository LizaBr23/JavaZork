package ZorkGame.threading;

import ZorkGame.models.Item;
import ZorkGame.utils.GenericClass;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ParallelInventorySearch {

    public static List<Item> searchByPartialNameParallel(
            GenericClass<Item> inventory, String searchTerm) {

        // parallelStream() automatically uses multiple threads
        return inventory.getAll().parallelStream()
            .filter(item -> item.getName().toLowerCase()
                               .contains(searchTerm.toLowerCase()))
            .collect(Collectors.toList());
    }





}
