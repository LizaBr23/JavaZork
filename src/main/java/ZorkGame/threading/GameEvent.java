package ZorkGame.threading;

//for repeating timed events
public interface GameEvent {
    void execute();
    String getDescription();
    boolean shouldRepeat();
    long getIntervalMs();
}
