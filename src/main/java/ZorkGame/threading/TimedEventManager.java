package ZorkGame.threading;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;


public class TimedEventManager {
    private final ScheduledExecutorService scheduler;
    private final List<ScheduledFuture<?>> scheduledTasks;

    public TimedEventManager() {
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.scheduledTasks = new ArrayList<>();
    }

    public void scheduleRepeating(GameEvent event) {
        if (!event.shouldRepeat()) {
            throw new IllegalArgumentException("Event must be repeating");
        }

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
            () -> {
                try {
                    event.execute();
                } catch (Exception e) {
                    System.err.println("Error executing repeating event: " + event.getDescription());
                    e.printStackTrace();
                }
            },
            event.getIntervalMs(),
            event.getIntervalMs(),
            TimeUnit.MILLISECONDS
        );
        scheduledTasks.add(future);
    }

    public void cancelAll() {
        for (ScheduledFuture<?> task : scheduledTasks) {
            task.cancel(false);
        }
        scheduledTasks.clear();
    }

    public void shutdown() {
        cancelAll();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
