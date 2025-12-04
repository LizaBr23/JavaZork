package ZorkGame.threading;

import ZorkGame.models.Character;
import ZorkGame.enums.AchievementType;
import java.util.Random;

public class HintEvent implements GameEvent {
    private final Character player;
    private final Random random = new Random();
    private final long intervalMs;

    private static final String[] HINTS = {
        "\n[Hint] Don't forget to explore all the rooms to discover new items!",
        "\n[Hint] Try talking to NPCs - they might have useful items for sale.",
        "\n[Hint] Some tools can be used on raw materials to create ingredients.",
        "\n[Hint] Check your recipes to see what ingredients you still need.",
        "\n[Hint] Visiting new rooms earns you coins!",
        "\n[Hint] Type 'achievements' to see your progress.",
        "\n[Hint] You can save your game at any time with the 'save' command.",
        "\n[Hint] Use the map to see where you've been and where you can go."
    };

    public HintEvent(Character player, long intervalMs) {
        this.player = player;
        this.intervalMs = intervalMs;
    }

    @Override
    public void execute() {
        StringBuilder hint = new StringBuilder();

        if (player.getUnlockedAchievements().isEmpty()) {
            hint.append("\n[Hint] Start exploring to unlock achievements!");
        } else if (!player.getUnlockedAchievements().contains(AchievementType.CARTOGRAPHER)) {
            hint.append("\n[Hint] Try opening the map to see where you've been!");
        } else if (!player.getUnlockedAchievements().contains(AchievementType.SOCIALITE)) {
            hint.append("\n[Hint] Have you talked to all the NPCs yet?");
        } else {
            // Random general hint
            hint.append(HINTS[random.nextInt(HINTS.length)]);
        }

        System.out.println(hint);
    }

    @Override
    public String getDescription() {
        return "Periodic Hint System";
    }

    @Override
    public boolean shouldRepeat() {
        return true;
    }

    @Override
    public long getIntervalMs() {
        return intervalMs;
    }
}
