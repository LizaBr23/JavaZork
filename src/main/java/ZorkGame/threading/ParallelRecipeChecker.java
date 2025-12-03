package ZorkGame.threading;

import ZorkGame.models.Recipe;
import java.util.List;
import java.util.concurrent.*;
import java.util.ArrayList;

public class ParallelRecipeChecker {

    // Thread pool for parallel execution
    private final ExecutorService executor;

    public ParallelRecipeChecker(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    public List<String> checkIngredientInParallel(List<Recipe> recipes,
                                                   String ingredientName)
            throws InterruptedException, ExecutionException {

        // Create a task for each recipe
        List<Callable<String>> tasks = new ArrayList<>();

        for (Recipe recipe : recipes) {
            Callable<String> task = () -> {
                // This runs in a worker thread
                if (recipe.getIngredient().contains(ingredientName)) {
                    return recipe.getName();
                }
                return null;
            };
            tasks.add(task);
        }

        // Execute all tasks in parallel
        List<Future<String>> futures = executor.invokeAll(tasks);

        // Collect results
        List<String> matchingRecipes = new ArrayList<>();
        for (Future<String> future : futures) {
            String result = future.get();
            if (result != null) {
                matchingRecipes.add(result);
            }
        }

        return matchingRecipes;
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
