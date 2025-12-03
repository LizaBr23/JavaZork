package ZorkGame.models;

import java.io.Serializable;

public interface GameEntity extends Serializable {
    String getName();
    Room getLocation();
    String getDescription();
}
