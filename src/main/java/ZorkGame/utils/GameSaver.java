package ZorkGame.utils;

import java.io.*;
import ZorkGame.exceptions.GameLoadException;
import ZorkGame.exceptions.GameSaveException;
import ZorkGame.models.Character;

public class GameSaver {

    public static void saveGame(Character player, String filename) throws GameSaveException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            out.writeObject(player);
            System.out.println("Game saved successfully to " + filename + "!");
        } catch (IOException e) {
            throw new GameSaveException(
                "Failed to save game to '" + filename + "'. Check file permissions and disk space.", e);
        }
    }

    public static Character loadGame(String filename) throws GameLoadException {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(filename))) {
            Character player = (Character) in.readObject();
            System.out.println("Game loaded successfully from " + filename + "!");
            return player;
        } catch (FileNotFoundException e) {
            throw new GameLoadException(
                "Save file '" + filename + "' not found. Please check the filename.", e);
        } catch (IOException e) {
            throw new GameLoadException(
                "Failed to load game from '" + filename + "'. The save file may be corrupted.", e);
        } catch (ClassNotFoundException e) {
            throw new GameLoadException(
                "Save file '" + filename + "' is incompatible with this version of the game.", e);
        }
    }
}