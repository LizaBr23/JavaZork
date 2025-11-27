package ZorkGame;

import java.io.Serializable;
import java.util.List;

// Interface for NPC
public interface DialogueHandler extends Serializable {

    // Get dialogue options
    List<String> getOptions(boolean hasObject);
    String getResponse(int choice, boolean hasObject);
    String getDescription(boolean hasObject);
}
