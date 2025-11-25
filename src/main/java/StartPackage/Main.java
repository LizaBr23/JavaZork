package StartPackage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name  = scanner.nextLine();
        ZorkULGame game = new ZorkULGame(name);
        game.play();
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
//            out.writeObject(game);
//            System.out.println("Object has been serialized to person.ser");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Deserialize the object from the file
//        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("person.ser"))) {
//            ZorkULGame deserializedPerson = (ZorkULGame) in.readObject();
//            System.out.println("Object has been deserialized:");
//            Room.getDescription();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }


    }
}
