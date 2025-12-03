package ZorkGame.dialogue;

import java.io.Serializable;
import java.util.List;

// Interface for NPC
public interface DialogueHandler extends Serializable {

    List<String> getOptions(boolean hasObject);
    String getResponse(int choice, boolean hasObject);
    String getDescription(boolean hasObject);
}
