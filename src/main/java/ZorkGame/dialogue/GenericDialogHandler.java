package ZorkGame.dialogue;

import java.io.Serial;
import java.util.List;
import ZorkGame.enums.NPCType;

public class GenericDialogHandler implements DialogueHandler {

    @Serial
    private static final long serialVersionUID = 20L;

    private boolean heardStory = false;
    private final String BASE_DESCRIPTION;
    private final String WITH_OBJECT_DESCRIPTION;
    private final List<String> FIRST_OPTIONS;
    private final List<String> AFTER_STORY_OPTIONS;
    private final String STORY_TEXT;

    public GenericDialogHandler(NPCType npcType) {
        this.BASE_DESCRIPTION = npcType.getBaseDescription();
        this.WITH_OBJECT_DESCRIPTION = npcType.getWithObjectDescription();
        this.FIRST_OPTIONS = npcType.getFirstOptions();
        this.AFTER_STORY_OPTIONS = npcType.getAfterStoryOptions();
        this.STORY_TEXT = npcType.getStoryText();
    }

    // Create GenericDialogHandler from NPCType enum
    public static GenericDialogHandler fromNPCType(NPCType npcType) {
        return new GenericDialogHandler(npcType);
    }

    @Override
    public String getDescription(boolean hasObject) {
        return hasObject ? WITH_OBJECT_DESCRIPTION : BASE_DESCRIPTION;
    }

    @Override
    public List<String> getOptions(boolean hasObject) {
        if (!hasObject) {
            return List.of(
                    "1. Try talking",
                    "2. Leave"
            );
        }

        return heardStory ? AFTER_STORY_OPTIONS : FIRST_OPTIONS;
    }

    @Override
    public String getResponse(int choice, boolean hasObject) {

        if (!hasObject) {
            if (choice == 1) return "They refuse to talk to you.";
            if (choice == 2) return "You walk away.";
            return "Invalid choice.";
        }

        if (!heardStory) {
            if (choice == 1) {
                heardStory = true;
                return STORY_TEXT;
            }else if (choice == 2) {
                return "\nThey smile slightly.\n\"I'm doing well, thank you.\"";
            } else if (choice == 3) {
                return "\nYou say goodbye and they nod.";
            }
            return "Invalid choice.";
        }

        if (choice == 1) {
            return "\n--- Shop ---";
        } else if (choice == 2) {
            return "\n\"Glad we're getting along. These items are quality.\"";
        } else if (choice == 3) {
            return "\nFarewell.";
        }

        return "Invalid choice.";
    }
}
