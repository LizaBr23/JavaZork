package ZorkGame.dialogue;

import java.io.Serial;
import java.util.List;
import ZorkGame.enums.NPCType;

public class GenericDialogHandler implements DialogueHandler {

    @Serial
    private static final long serialVersionUID = 20L;

    private boolean heardStory = false;
    private String baseDescription;
    private String withObjectDescription;
    private List<String> firstOptions;
    private List<String> afterStoryOptions;
    private String storyText;

    public GenericDialogHandler(NPCType npcType) {
        this.baseDescription = npcType.getBaseDescription();
        this.withObjectDescription = npcType.getWithObjectDescription();
        this.firstOptions = npcType.getFirstOptions();
        this.afterStoryOptions = npcType.getAfterStoryOptions();
        this.storyText = npcType.getStoryText();
    }

    // Create GenericDialogHandler from NPCType enum
    public static GenericDialogHandler fromNPCType(NPCType npcType) {
        return new GenericDialogHandler(npcType);
    }
    public void setBaseDescription(String baseDescription) {
        this.baseDescription = baseDescription;
    }

    public void setWithObjectDescription(String withObjectDescription) {
        this.withObjectDescription = withObjectDescription;
    }

    public void setFirstOptions(List<String> firstOptions) {
        this.firstOptions = firstOptions;
    }

    public void setAfterStoryOptions(List<String> afterStoryOptions) {
        this.afterStoryOptions = afterStoryOptions;
    }

    public void setStoryText(String storyText) {
        this.storyText = storyText;
    }

    @Override
    public String getDescription(boolean hasObject) {
        return hasObject ? withObjectDescription : baseDescription;
    }

    @Override
    public List<String> getOptions(boolean hasObject) {
        if (!hasObject) {
            return List.of(
                    "1. Try talking",
                    "2. Leave"
            );
        }

        return heardStory ? afterStoryOptions : firstOptions;
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
                return storyText;
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
