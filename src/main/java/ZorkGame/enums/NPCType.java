package ZorkGame.enums;

import java.util.List;

public enum NPCType {
    BOB(
        "Bob the bob.\nOld fart sitting in the pub, some people say he sells good tools.",
        "Bob the bob.\nOld fart sitting in the pub.\nHe seems to like you for some mysterious reason.",
        List.of(
            "1. Listen to Bob's story",
            "2. How are you?",
            "3. Never mind, goodbye"
        ),
        List.of(
            "1. Buy tools from Bob",
            "2. Chat with Bob a bit more",
            "3. Leave"
        ),
        "\n--- Bob's story ---\n\"Back in my day, I used to be an adventurer like you. These tools? I forged 'em myself.\nEach one has a story, and each one will serve you well if you respect 'em.\"\n"
    ),

    ALICE(
        "Alice the Enchantress.\nA mysterious woman with a magical aura. She watches you carefully.",
        "Alice the Enchantress.\nHer eyes glow. She smiles mysteriously, clearly interested in you.",
        List.of(
            "1. Ask about magic",
            "2. How are you?",
            "3. Goodbye"
        ),
        List.of(
            "1. Shop things",
            "2. Talk again",
            "3. Leave"
        ),
        "\n--- Alice's Story ---\nAlice whispers ancient words.\n\"The tools I have store mysterious ancient magic. Use them wisely, and they will serve you well.\"\nShe touches your shoulder gently.\n"
    );

    private final String baseDescription;
    private final String withObjectDescription;
    private final List<String> firstOptions;
    private final List<String> afterStoryOptions;
    private final String storyText;

    NPCType(String baseDescription, String withObjectDescription,
            List<String> firstOptions, List<String> afterStoryOptions,
            String storyText) {
        this.baseDescription = baseDescription;
        this.withObjectDescription = withObjectDescription;
        this.firstOptions = firstOptions;
        this.afterStoryOptions = afterStoryOptions;
        this.storyText = storyText;
    }

    public String getBaseDescription() {
        return baseDescription;
    }

    public String getWithObjectDescription() {
        return withObjectDescription;
    }

    public List<String> getFirstOptions() {
        return firstOptions;
    }

    public List<String> getAfterStoryOptions() {
        return afterStoryOptions;
    }

    public String getStoryText() {
        return storyText;
    }
}
