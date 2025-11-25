import java.io.Serializable;
import java.util.*;


interface DialogueHandler extends Serializable {
    List<String> getOptions(boolean hasObject);
    String getResponse(int choice, boolean hasObject);
    String getDescription(boolean hasObject);
}

//generic
public class NPC implements Serializable {
    private static final long serialVersionUID = 11L;

    protected String name;
    protected Room location;
    protected Map<String, Integer> inventory; // Tool/ItemName => price
    protected boolean isTalking;
    protected DialogueHandler dialogueHandler;
    protected String itemWantedToTalk;


    public NPC(String name, Room location, DialogueHandler handler, String itemWantedToTalk) {
        this.name = name;
        this.location = location;
        this.inventory = new HashMap<>();
        this.isTalking = false;
        this.dialogueHandler = handler;
        this.itemWantedToTalk = itemWantedToTalk;
    }

    public String getItemWantedToTalk(){
        return itemWantedToTalk;
    }

    public String getName() {
        return name;
    }

    public Room getLocation() {
        return location;
    }

    public void addToInventory(String itemName, int price) {
        inventory.put(itemName, price);
    }

    //if selling the item
    public boolean hasItem(String itemName) {
        return inventory.containsKey(itemName);
    }

    public int getItemPrice(String itemName) {
        return inventory.getOrDefault(itemName, -1);
    }

    public Set<String> getAvailableItems() {
        return inventory.keySet();
    }

   //for dialog
    public void setTalking(boolean talking) {
        this.isTalking = talking;
    }

    public boolean isTalking() {
        return isTalking;
    }

    public List<String> getDialogueOptions(boolean hasObject) {
        return dialogueHandler.getOptions(hasObject);
    }

    public String getDialogueResponse(int choice, boolean hasObject) {
        return dialogueHandler.getResponse(choice, hasObject);
    }


    public String getDescription(boolean hasObject) {
        return dialogueHandler.getDescription(hasObject);
    }
}

class BobDialogueHandler implements DialogueHandler {
    private static final long serialVersionUID = 12L;

    private boolean heardStory = false;

    private static final String BASE_DESCRIPTION =
            "Bob the bob.\n" +
                    "Old fart sitting in the pub, some people say he sells good tools.";

    private static final String WITH_OBJECT_DESCRIPTION =
            "Bob the bob.\n" +
                    "Old fart sitting in the pub, some people say he sells good tools.\n"+"He seems to like you for some mysterious reason.";

    private static final String STORY_1 =
            "\n--- Bob's story ---\n" +
                    "\"Back in my day, I used to be an adventurer like you. These tools? I forged 'em myself.\n" +
                    "Each one has a story, and each one will serve you well if you respect 'em.\"\n";

    @Override
    public String getDescription(boolean hasObject) {
        if (hasObject) {
            return WITH_OBJECT_DESCRIPTION;
        }
        return BASE_DESCRIPTION;
    }

    @Override
    public List<String> getOptions(boolean hasObject) {
        List<String> options = new ArrayList<>();

        if (!hasObject) {
            options.add("1. Try to talk to Bob (he gives you a look of hatred)");
            options.add("2. Leave him alone");
        } else {
            if (!heardStory) {
                options.add("1. Listen to Bob's story");
                options.add("2. How are you?");
                options.add("3. Never mind, goodbye");
            } else {
                options.add("1. Buy tools from Bob");
                options.add("2. Chat with Bob a bit more");
                options.add("3. Leave");
            }
        }

        return options;
    }

    @Override
    public String getResponse(int choice, boolean hasObject) {
        if (!hasObject) {
            if (choice == 1) {
                return "\nBob ignores you.\nGet lost. Come back when you have something worth my time.\nYou go away";
            } else if (choice == 2) {
                return "\nYou decide to leave Bob alone.";
            }
            return "Invalid choice.";
        }

        //has object
        if (!heardStory) {
            if (choice == 1) {
                heardStory = true;
                return STORY_1;
            } else if (choice == 2) {
                return "\nBob smiles.\n\"I'm doing better now, friend. Much better.\"";
            } else if (choice == 3) {
                return "\nYou say goodbye and Bob nods.";
            }
        } else {
            //already heard the story
            if (choice == 1) {
                return "\n--- Bob's Shop ---\n";
            } else if (choice == 2) {
                return "\"Glad we're getting along. These tools won't disappoint you.\"";
            } else if (choice == 3) {
                return "\nYou left Bob.";
            }
        }

        return "Invalid choice.";
    }
}


class AliceDialogueHandler implements DialogueHandler {
    private static final long serialVersionUID = 13L;

    private boolean heardStory = false;

    private static final String BASE_DESCRIPTION =
            "Alice the Enchantress.\n" +
                    "A mysterious woman with magical aura. She watches you carefully.";

    private static final String WITH_OBJECT_DESCRIPTION =
            "Alice the Enchantress.\n" +
                    "Her eyes glow. She smiles mysteriously, clearly interested in you.";

    private static final String STORY_LORE =
            "\n--- Alice's Story ---\n" +
                    "Alice whispers ancient words.\n" +
                    "\"The tools I have store mysterious ancient magic. Use them wisely, and they will serve you well.\"\n" +
                    "She touches your shoulder gently.";

    @Override
    public String getDescription(boolean hasObject) {
        if (hasObject) {
            return WITH_OBJECT_DESCRIPTION;
        }
        return BASE_DESCRIPTION;
    }

    @Override
    public List<String> getOptions(boolean hasObject) {
        List<String> options = new ArrayList<>();

        if (!hasObject) {
            options.add("1. Try talking to Alice");
            options.add("2. Leave");
        } else {
            if (!heardStory) {
                options.add("1. Ask about magic");
                options.add("2. What do you sell?");
                options.add("3. Goodbye");
            } else {
                options.add("1. Shop things");
                options.add("2. Talk again");
                options.add("3. Leave");
            }
        }

        return options;
    }

    @Override
    public String getResponse(int choice, boolean hasObject) {
        if (!hasObject) {
            if (choice == 1) {
                return "\nAlice eyes you silently but says nothing.\nShe seems to want something from you first.";
            } else if (choice == 2) {
                return "\nYou go away slowly. Alice watches you leaving.";
            }
            return "Invalid choice.";
        }

        if (!heardStory) {
            if (choice == 1) {
                heardStory = true;
                return STORY_LORE;
            } else if (choice == 2) {
                return "\nAlice nods.\n\"I have rare recipes. Interested?\"";
            } else if (choice == 3) {
                return "\nAlice nods farewell.";
            }
        } else {
            if (choice == 1) {
                return "\n--- Alice's Shop ---\n" +
                        "Alice displays everything she has carefully.\n" +
                        "\"Choose wisely.\"";
            } else if (choice == 2) {
                return "\nAlice smiles mysteriously.\n" +
                        "\"That is for another time, perhaps.\"";
            } else if (choice == 3) {
                return "\nAlice watches you leave.";
            }
        }

        return "Invalid choice.";
    }
}
//
//class MarkusDialogueHandler implements DialogueHandler {
//    private static final long serialVersionUID = 14L;
//
//    private boolean hasHeardTales = false;
//
//    private static final String BASE_DESCRIPTION =
//            "Markus the Merchant.\n" +
//                    "A gruff, no-nonsense trader. He barely glances at you.";
//
//    private static final String WITH_OBJECT_DESCRIPTION =
//            "Markus the Merchant.\n" +
//                    "His interest perks up when he sees you. Dollar signs almost appear in his eyes.";
//
//    private static final String STORY_TALES =
//            "\n--- Markus's Tales ---\n" +
//                    "Markus chuckles and leans back.\n" +
//                    "\"I've traveled every corner of this realm. Seen things you wouldn't believe.\n" +
//                    "And these? These are the finest tools I ever found.\"\n" +
//                    "\"Fair prices, honest deals. That's the merchant way.\"";
//
//    @Override
//    public String getDescription(boolean hasObject) {
//        if (hasObject) {
//            return WITH_OBJECT_DESCRIPTION;
//        }
//        return BASE_DESCRIPTION;
//    }
//
//    @Override
//    public List<String> getOptions(boolean hasObject) {
//        List<String> options = new ArrayList<>();
//
//        if (!hasObject) {
//            options.add("1. Talk to Markus");
//            options.add("2. Move on");
//        } else {
//            if (!hasHeardTales) {
//                options.add("1. Hear his stories");
//                options.add("2. Show me your goods");
//                options.add("3. Later");
//            } else {
//                options.add("1. See what you're selling");
//                options.add("2. Tell me more");
//                options.add("3. Goodbye");
//            }
//        }
//
//        return options;
//    }
//
//    @Override
//    public String getResponse(int choice, boolean hasObject) {
//        if (!hasObject) {
//            if (choice == 1) {
//                return "\nMarkus barely looks at you.\n\"You got nothing I want. Come back with something valuable.\"";
//            } else if (choice == 2) {
//                return "\nYou walk away from Markus.";
//            }
//            return "Invalid choice.";
//        }
//
//        if (!hasHeardTales) {
//            if (choice == 1) {
//                hasHeardTales = true;
//                return STORY_TALES;
//            } else if (choice == 2) {
//                return "\nMarkus spreads his wares.\n\"Best collection in three kingdoms, friend.\"";
//            } else if (choice == 3) {
//                return "\nMarkus shrugs and returns to his ledger.";
//            }
//        } else {
//            if (choice == 1) {
//                return "\n--- Markus's Inventory ---\n" +
//                        "Markus arranges his tools proudly.\n" +
//                        "\"Everything here is quality. Name your poison.\"";
//            } else if (choice == 2) {
//                return "\nMarkus grins.\n" +
//                        "\"Smart choice, listening to a seasoned trader.\"";
//            } else if (choice == 3) {
//                return "\nMarkus nods. \"May fortune favor you.\"";
//            }
//        }
//
//        return "Invalid choice.";
//    }
//}