package StartPackage;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

public class GenericDialogHandler implements DialogueHandler {

    private static final long serialVersionUID = 20L;

    private boolean heardStory = false;
    private String baseDescription;
    private String withObjectDescription;
    private List<String> firstOptions;
    private List<String> afterStoryOptions;
    private String storyText;

    public GenericDialogHandler() {}

    public static GenericDialogHandler fromJson(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filePath), GenericDialogHandler.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load NPC file: " + filePath, e);
        }
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

        // If story not heard yet → option 1 triggers story
        if (!heardStory) {
            if (choice == 1) {
                heardStory = true;
                return storyText;
            }
        }

        return "Response for choice " + choice;
    }
}
