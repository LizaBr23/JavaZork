package ZorkGame;

import ZorkGame.models.Character;
import ZorkGame.models.Room;
import ZorkGame.models.Item;
import ZorkGame.models.Recipe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CharacterTest {
    private Room testRoom;
    private Character testCharacter;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp(){
        testRoom = new Room("Test Room");
        testCharacter = new Character("TestPlayer", testRoom );
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreConsole() {
        System.setOut(originalOut);
    }

    @Test
    public void testListInventory_givenEmpty(){
        String message = testCharacter.listInventory();
        assertEquals("\nYou have no items with you.",message);
    }

    @Test
    public void testListInventory_givenOneItem(){
        Item potion = new Item("potion", "potion", testRoom,1);
        testRoom.addItem(potion);
        testCharacter.takeItem("potion");

        String message = testCharacter.listInventory();
        assertTrue(message.startsWith("\nYour inventory: "));
        assertTrue(message.contains("potion"));
        assertFalse(message.contains("x2"));
    }

    @Test
    public void testListInventory_givenMultipleItems(){
        Item potion = new Item("potion", "potion", testRoom,1);
        Item potion2 = new Item("potion2", "potion2", testRoom,2);
        Item potion3 = new Item("potion3", "potion3", testRoom,3);
        testRoom.addItem(potion);
        testRoom.addItem(potion2);
        testRoom.addItem(potion3);
        testCharacter.takeItem("potion");
        testCharacter.takeItem("potion2");
        testCharacter.takeItem("potion3");


        String message = testCharacter.listInventory();
        assertTrue(message.startsWith("\nYour inventory: "));
        assertTrue(message.contains("potion"));
        assertTrue(message.contains("potion2"));
        assertTrue(message.contains("potion3"));
        assertFalse(message.contains("x2"));
    }

    @Test
    public void testListInventory_givenMultipleSameItems(){
        Item potion = new Item("potion", "potion", testRoom,1);
        Item potion2 = new Item("potion", "potion", testRoom,2);
        Item potion3 = new Item("potion", "potion", testRoom,3);
        testRoom.addItem(potion);
        testRoom.addItem(potion2);
        testRoom.addItem(potion3);
        testCharacter.takeItem("potion");
        testCharacter.takeItem("potion");
        testCharacter.takeItem("potion");


        String message = testCharacter.listInventory();
        assertTrue(message.startsWith("\nYour inventory: "));
        assertTrue(message.contains("potion"));
        assertTrue(message.contains("x3"));
    }


    @Test
    public void testListInventory_givenMixedItems(){
        Item potion = new Item("potion", "potion", testRoom,1);
        Item potion2 = new Item("potion", "potion", testRoom,2);
        Item fire = new Item("fire", "fire", testRoom,3);
        testRoom.addItem(potion);
        testRoom.addItem(potion2);
        testRoom.addItem(fire);
        testCharacter.takeItem("potion");
        testCharacter.takeItem("potion");
        testCharacter.takeItem("fire");


        String message = testCharacter.listInventory();
        assertTrue(message.startsWith("\nYour inventory: "));
        assertTrue(message.contains("potion"));
        assertTrue(message.contains("x2"));
        assertTrue(message.contains("fire"));
    }

    @Test
    public void testListInventory_afterDroppingItem(){
        Item potion = new Item("potion", "potion", testRoom,1);
        testRoom.addItem(potion);
        testCharacter.takeItem("potion");

        String messageBefore = testCharacter.listInventory();
        assertTrue(messageBefore.startsWith("\nYour inventory: "));
        assertTrue(messageBefore.contains("potion"));
        assertFalse(messageBefore.contains("x2"));

        testCharacter.dropItem("potion");

        String messageAfter = testCharacter.listInventory();
        assertEquals("\nYou have no items with you.", messageAfter);
    }



    @Test
    public void testCheckRecipes_givenEmpty(){
        outContent.reset();
        testCharacter.checkRecipes();

        String output = outContent.toString();
        assertTrue(output.contains("=== Recipe Progress ==="));
        assertTrue(output.contains("You don't have any recipes yet."));
    }

    @Test
    public void testCheckRecipes_givenOneRecipeNoIngredients(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add("water");
        ingredients.add("sugar");

        Recipe recipe = new Recipe("healingPotion", "recipe", testRoom, 1, 2, 0, ingredients);
        testRoom.addItem(recipe);
        testCharacter.takeItem("healingPotion");

        outContent.reset();
        testCharacter.checkRecipes();

        String output = outContent.toString();
        assertTrue(output.contains("=== Recipe Progress ==="));
        assertTrue(output.contains("healingPotion: 0/2 ingredients collected"));
        assertTrue(output.contains("Needs: water sugar"));
    }

    @Test
    public void testCheckRecipes_givenOneRecipePartialCollectedIngredients(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add("water");
        ingredients.add("sugar");
        ingredients.add("herbs");

        Recipe recipe = new Recipe("healingPotion", "recipe", testRoom, 1, 3, 0, ingredients);
        testRoom.addItem(recipe);
        testCharacter.takeItem("healingPotion");

        recipe.addIngredient("water");

        outContent.reset();
        testCharacter.checkRecipes();

        String output = outContent.toString();
        assertTrue(output.contains("=== Recipe Progress ==="));
        assertTrue(output.contains("healingPotion: 1/3 ingredients collected"));
        assertTrue(output.contains("Needs: water sugar herbs"));
    }

    @Test
    public void testCheckRecipes_givenCompleteRecipe(){
        List<String> ingredients = new ArrayList<>();
        ingredients.add("water");
        ingredients.add("sugar");

        Recipe recipe = new Recipe("healingPotion", "recipe", testRoom, 1, 2, 0, ingredients);
        testRoom.addItem(recipe);
        testCharacter.takeItem("healingPotion");

        recipe.addIngredient("water");
        recipe.addIngredient("sugar");

        outContent.reset();
        testCharacter.checkRecipes();

        String output = outContent.toString();
        assertTrue(output.contains("=== Recipe Progress ==="));
        assertTrue(output.contains("healingPotion: 2/2 ingredients collected"));
        assertTrue(output.contains("Needs: water sugar"));
    }

    @Test
    public void testCheckRecipes_givenMultipleRecipes(){
        List<String> ingredients1 = new ArrayList<>();
        ingredients1.add("water");
        ingredients1.add("sugar");

        Recipe recipe1 = new Recipe("healingPotion", "recipe", testRoom, 1, 2, 0, ingredients1);
        testRoom.addItem(recipe1);
        testCharacter.takeItem("healingPotion");

        List<String> ingredients2 = new ArrayList<>();
        ingredients2.add("fire");
        ingredients2.add("oil");
        ingredients2.add("cloth");

        Recipe recipe2 = new Recipe("firePotion", "recipe", testRoom, 2, 3, 0, ingredients2);
        testRoom.addItem(recipe2);
        testCharacter.takeItem("firePotion");

        recipe1.addIngredient("water");
        recipe1.addIngredient("sugar");

        outContent.reset();
        testCharacter.checkRecipes();

        String output = outContent.toString();
        assertTrue(output.contains("=== Recipe Progress ==="));
        assertTrue(output.contains("healingPotion: 2/2 ingredients collected"));
        assertTrue(output.contains("firePotion: 0/3 ingredients collected"));
        assertTrue(output.contains("Needs: water sugar"));
        assertTrue(output.contains("Needs: fire oil cloth"));
    }


}
