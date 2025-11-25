//package StartPackage;
//
//import javafx.application.Application;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import java.io.*;
//
//public class FileOpener extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Resource File");
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
//                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
//                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
//                new FileChooser.ExtensionFilter("All Files", "*.*"));
//
//        File selectedFile = fileChooser.showOpenDialog(primaryStage);
//
//        if (selectedFile != null) {
//            System.out.println("File you chose has a name: " + selectedFile.getName());
//            try (DataInputStream in = new DataInputStream(new FileInputStream(selectedFile))) {
//                // Do something with the file
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
////    public static void main(String[] args) {
////        launch(args); // Launches the JavaFX app and calls start()
////    }
//}
