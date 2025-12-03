package ZorkGame.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Background;
import ZorkGame.game.ZorkULGame;
import ZorkGame.commands.Command;
import ZorkGame.commands.Parser;
import ZorkGame.models.Character;
import ZorkGame.models.NPC;
import ZorkGame.models.Room;
import java.io.PrintStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Objects;

public class GUI extends Application {
    // Style constants
    private static final String DARK_BG = "rgba(20, 20, 20, 0.3)";
    private static final String BORDER_COLOR = "rgba(60, 60, 60, 0.6)";
    private static final String TEXT_COLOR = "#e0e0e0";

    private static final String TEXT_BUTTON_STYLE =
        "-fx-background-color: " + DARK_BG + "; " +
        "-fx-text-fill: " + TEXT_COLOR + "; " +
        "-fx-border-color: " + BORDER_COLOR + "; " +
        "-fx-border-width: 1px; " +
        "-fx-border-radius: 5px; " +
        "-fx-background-radius: 5px; " +
        "-fx-font-size: 13px; " +
        "-fx-padding: 8px 15px;";

    private static final String IMAGE_BUTTON_STYLE =
        "-fx-background-color: transparent; " +
        "-fx-border-color: transparent; " +
        "-fx-padding: 0;";

    // Game components
    private ZorkULGame game;
    private Parser parser;
    private TextField inputField;
    private TextArea textArea;
    private BorderPane root;

    // Action result panel
    private TextArea actionResultArea;
    private ScrollPane actionResultPane;
    private PrintStream mainOutputStream;

    // Dialogue system
    private NPC currentNPC;
    private boolean inShop;
    private HBox dialogueButtons;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hecate Game");

        root = new BorderPane();
        Scene scene = new Scene(root);

        setupTextArea();
        setupActionResultArea();
        setupInputField();
        setupButtons();
        setupLayout(scene);
        setupGameIO();

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        updateBackground("sunfield");
        startBackgroundMusic();
    }

    private void setupTextArea() {
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle(
            "-fx-control-inner-background: " + DARK_BG + "; " +
            "-fx-background-color: " + DARK_BG + "; " +
            "-fx-text-fill: " + TEXT_COLOR + "; " +
            "-fx-font-size: 14px; " +
            "-fx-font-family: 'Consolas', 'Courier New', 'Monospaced'; " +
            "-fx-background: " + DARK_BG + "; " +
            "-fx-box-border: transparent; " +
            "-fx-focus-color: transparent; " +
            "-fx-text-box-border: transparent; " +
            "-fx-background-insets: 0;"
        );
        textArea.setOpacity(1.0);
    }

    private void setupActionResultArea() {
        actionResultArea = new TextArea();
        actionResultArea.setEditable(false);
        actionResultArea.setWrapText(true);
        actionResultArea.setStyle(
            "-fx-control-inner-background: " + DARK_BG + "; " +
            "-fx-background-color: " + DARK_BG + "; " +
            "-fx-text-fill: " + TEXT_COLOR + "; " +
            "-fx-font-size: 14px; " +
            "-fx-font-family: 'Consolas', 'Courier New', 'Monospaced'; " +
            "-fx-background: " + DARK_BG + "; " +
            "-fx-box-border: transparent; " +
            "-fx-focus-color: transparent; " +
            "-fx-text-box-border: transparent; " +
            "-fx-background-insets: 0;"
        );
        actionResultArea.setOpacity(1.0);

        actionResultPane = new ScrollPane(actionResultArea);
        actionResultPane.setFitToWidth(true);
        actionResultPane.setFitToHeight(true);
        actionResultPane.setStyle(
            "-fx-background: " + DARK_BG + "; " +
            "-fx-background-color: " + DARK_BG + "; " +
            "-fx-border-color: " + BORDER_COLOR + "; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        );
        actionResultPane.setPadding(new Insets(5));
        actionResultPane.setOpacity(1.0);
        actionResultPane.setVisible(false);
        actionResultPane.setManaged(false);

        actionResultPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            actionResultArea.setPrefHeight(newVal.getHeight());
        });
    }

    private void setupInputField() {
        inputField = new TextField();
        inputField.setPromptText("Type your command here...");
        inputField.setPrefHeight(40);
        inputField.setStyle(
            "-fx-background-color: " + DARK_BG + "; " +
            "-fx-text-fill: " + TEXT_COLOR + "; " +
            "-fx-prompt-text-fill: #808080; " +
            "-fx-border-color: " + BORDER_COLOR + "; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-font-size: 14px;"
        );

        inputField.setOnAction(e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                textArea.appendText("> " + text + "\n");
                processCommand(text);
                inputField.clear();
            }
        });
    }

    private void setupButtons() {
        // Top buttons
        HBox topButtons = new HBox(10);
        topButtons.setAlignment(Pos.CENTER_LEFT);
        topButtons.setPadding(new Insets(10));
        topButtons.getChildren().addAll(
            createTextButton("Quit", e -> Platform.exit()),
            createTextButton("Save", "save"),
            createTextButton("Load", "load")
        );
        root.setTop(topButtons);

        // Right side buttons
        VBox rightButtons = new VBox(15);
        rightButtons.setAlignment(Pos.CENTER_RIGHT);
        rightButtons.setPadding(new Insets(10));
        rightButtons.getChildren().addAll(
            createActionButton("scroll.png", "recipes", "View your recipes and crafting combinations"),
            createActionButton("inventory.png", "inventory", "Check your inventory and items"),
            createActionButton("game.png", "achievements", "View your game achievements"),
            createActionButton("game map.png", "map", "Display the game map"),
            createActionButton("questioning.png", "help", "Show help and available commands"),
            createDirectionGrid()
        );
        root.setRight(rightButtons);
    }

    private GridPane createDirectionGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(createImageButton("north.png", "go north", 50, "Move north"), 1, 0);
        grid.add(createImageButton("west.png", "go west", 50, "Move west"), 0, 1);
        grid.add(createImageButton("east.png", "go east", 50, "Move east"), 2, 1);
        grid.add(createImageButton("south.png", "go south", 50, "Move south"), 1, 2);
        return grid;
    }

    private void setupLayout(Scene scene) {
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle(
            "-fx-background: " + DARK_BG + "; " +
            "-fx-background-color: " + DARK_BG + "; " +
            "-fx-border-color: " + BORDER_COLOR + "; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        );
        scrollPane.setPadding(new Insets(5));
        scrollPane.setOpacity(1.0);
        scrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            textArea.setPrefHeight(newVal.getHeight());
        });

        // Dialogue buttons
        dialogueButtons = new HBox(10);
        dialogueButtons.setAlignment(Pos.CENTER_LEFT);
        dialogueButtons.setPadding(new Insets(10));
        dialogueButtons.setVisible(false);

        // HBox to hold main text area and action result panel side by side
        HBox textAreasContainer = new HBox(10);
        textAreasContainer.setAlignment(Pos.CENTER_LEFT);
        textAreasContainer.getChildren().addAll(scrollPane, actionResultPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(actionResultPane, Priority.ALWAYS);

        // Bottom container
        VBox bottomContainer = new VBox(10);
        bottomContainer.setAlignment(Pos.TOP_LEFT);
        bottomContainer.getChildren().addAll(textAreasContainer, dialogueButtons, inputField);
        VBox.setVgrow(textAreasContainer, Priority.ALWAYS);

        // Center container with spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox centerContainer = new VBox(10);
        centerContainer.setAlignment(Pos.TOP_LEFT);
        centerContainer.setPadding(new Insets(10));
        centerContainer.getChildren().addAll(spacer, bottomContainer);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(bottomContainer, Priority.ALWAYS);

        root.setCenter(centerContainer);

        // Bind sizes
        scrollPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.4));
        scrollPane.maxWidthProperty().bind(scene.widthProperty().multiply(0.4));
        actionResultPane.prefWidthProperty().bind(scene.widthProperty().multiply(0.4));
        actionResultPane.maxWidthProperty().bind(scene.widthProperty().multiply(0.4));
        inputField.prefWidthProperty().bind(scene.widthProperty().multiply(0.4));
        inputField.maxWidthProperty().bind(scene.widthProperty().multiply(0.4));
        bottomContainer.prefHeightProperty().bind(scene.heightProperty().multiply(0.75));
        spacer.prefHeightProperty().bind(scene.heightProperty().multiply(0.25));

        // Force textarea transparency
        Platform.runLater(() -> {
            if (textArea.lookup(".content") != null) {
                textArea.lookup(".content").setStyle("-fx-background-color: " + DARK_BG + ";");
            }
            if (actionResultArea.lookup(".content") != null) {
                actionResultArea.lookup(".content").setStyle("-fx-background-color: " + DARK_BG + ";");
            }
        });
    }

    private void setupGameIO() {
        // Redirect System.out to textarea
        OutputStream out = new OutputStream() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                synchronized (buffer) {
                    char c = (char) b;
                    if (c == '\n') {
                        flush();
                    } else {
                        buffer.append(c);
                    }
                }
            }

            @Override
            public void write(byte[] b, int off, int len) {
                synchronized (buffer) {
                    buffer.append(new String(b, off, len));
                    if (buffer.indexOf("\n") >= 0) {
                        flush();
                    }
                }
            }

            @Override
            public void flush() {
                synchronized (buffer) {
                    if (buffer.length() > 0) {
                        String text = buffer.toString();
                        buffer.setLength(0);
                        Platform.runLater(() -> textArea.appendText(text));
                    }
                }
            }
        };

        mainOutputStream = new PrintStream(out, true);
        System.setOut(mainOutputStream);
        System.setErr(mainOutputStream);

        // Initialize game
        Character.setGuiMode(true);
        parser = new Parser(true);
        game = new ZorkULGame("Player");

        System.out.println();
        System.out.println("Welcome to the mysterious Hecate adventure!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        game.printCurrentRoom();
    }

    private void startBackgroundMusic() {
        try {
            String musicPath = getClass().getResource("/mp3/music.mp3").toExternalForm();
            Media sound = new Media(musicPath);
            MediaPlayer player = new MediaPlayer(sound);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();
        } catch (Exception e) {
            System.err.println("Could not load background music");
        }
    }

    // Helper methods for creating buttons
    private Button createTextButton(String text, String command) {
        Button button = new Button(text);
        button.setStyle(TEXT_BUTTON_STYLE);
        button.setOnAction(e -> {
            textArea.appendText("> " + command + "\n");
            processCommand(command);
        });
        return button;
    }

    private Button createTextButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(TEXT_BUTTON_STYLE);
        button.setOnAction(handler);
        return button;
    }

    private Button createImageButton(String imageName, String command) {
        return createImageButton(imageName, command, 40, null);
    }

    private Button createImageButton(String imageName, String command, String tooltipText) {
        return createImageButton(imageName, command, 40, tooltipText);
    }

    private Button createImageButton(String imageName, String command, int size) {
        return createImageButton(imageName, command, size, null);
    }

    private Button createImageButton(String imageName, String command, int size, String tooltipText) {
        Button button = new Button();
        button.setStyle(IMAGE_BUTTON_STYLE);

        Image image = new Image(getClass().getResourceAsStream("/ZorkGame/images/" + imageName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        button.setGraphic(imageView);

        button.setOnAction(e -> {
            textArea.appendText("> " + command + "\n");
            processCommand(command);
        });

        // Add tooltip
        if (tooltipText != null && !tooltipText.isEmpty()) {
            Tooltip tooltip = new Tooltip(tooltipText);
            button.setTooltip(tooltip);
        }

        return button;
    }

    private Button createActionButton(String imageName, String command, String tooltipText) {
        Button button = new Button();
        button.setStyle(IMAGE_BUTTON_STYLE);

        Image image = new Image(getClass().getResourceAsStream("/ZorkGame/images/" + imageName));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        button.setGraphic(imageView);

        button.setOnAction(e -> {
            showActionResult(command);
        });

        // Add tooltip
        if (tooltipText != null && !tooltipText.isEmpty()) {
            Tooltip tooltip = new Tooltip(tooltipText);
            button.setTooltip(tooltip);
        }

        return button;
    }

    private Button createDialogueButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(TEXT_BUTTON_STYLE);
        button.setOnAction(e -> action.run());
        return button;
    }

    // Command processing
    private void processCommand(String input) {
        // Hide action result panel when any regular command is executed
        hideActionResult();

        Command command = parser.parseCommandString(input);
        boolean shouldQuit = game.processCommand(command);

        if (shouldQuit) {
            inputField.setDisable(true);
            return;
        }

        updateBackground(game.getCurrentRoomName());
        checkForDialogue();
    }

    private void checkForDialogue() {
        Platform.runLater(() -> {
            String text = textArea.getText();
            if (!text.contains("DIALOGUE_START:")) return;

            int startIndex = text.lastIndexOf("DIALOGUE_START:");
            int endIndex = text.indexOf("\n", startIndex);
            if (endIndex == -1) endIndex = text.length();

            String npcName = text.substring(startIndex + 15, endIndex).trim();
            textArea.setText(text.substring(0, startIndex) + text.substring(endIndex));

            findAndTalkToNPC(npcName);
        });
    }

    private void findAndTalkToNPC(String npcName) {
        Room currentRoom = game.getPlayer().getCurrentRoom();
        for (NPC npc : currentRoom.getNPCs()) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                startNPCDialogue(npc);
                break;
            }
        }
    }

    private void updateBackground(String roomName) {
        String imageName = roomName.replace(" ", "").toLowerCase();
        try {
            Image image = new Image(getClass().getResourceAsStream("/ZorkGame/images/" + imageName + ".png"));
            BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
            );
            root.setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.err.println("Background image not found: " + imageName + ".png");
        }
    }

    // Dialogue system
    private void startNPCDialogue(NPC npc) {
        Objects.requireNonNull(npc, "NPC cannot be null");

        currentNPC = npc;
        inputField.setDisable(true);

        boolean hasItem = game.getPlayer().hasItem(npc.getItemWantedToTalk());

        textArea.appendText("\n=== " + npc.getName() + " ===\n");
        textArea.appendText(npc.getDescription(hasItem) + "\n\n");

        showDialogueOptions(npc.getDialogueOptions(hasItem));
    }

    private void showDialogueOptions(List<String> options) {
        if (options == null || options.isEmpty()) {
            endDialogue();
            return;
        }

        dialogueButtons.getChildren().clear();

        for (int i = 0; i < options.size(); i++) {
            final int choice = i + 1;
            String text = options.get(i);

            // Remove number prefix if present
            if (text.startsWith(choice + ". ")) {
                text = text.substring((choice + ". ").length());
            }

            dialogueButtons.getChildren().add(
                createDialogueButton(text, () -> handleDialogueChoice(choice))
            );
        }

        dialogueButtons.getChildren().add(
            createDialogueButton("Exit Conversation", this::endDialogue)
        );

        dialogueButtons.setVisible(true);
    }

    private void handleDialogueChoice(int choice) {
        if (currentNPC == null) {
            endDialogue();
            return;
        }

        if (inShop) {
            handleShopChoice(choice);
            return;
        }

        boolean hasItem = game.getPlayer().hasItem(currentNPC.getItemWantedToTalk());
        String response = currentNPC.getDialogueResponse(choice, hasItem);

        textArea.appendText("\n" + response + "\n\n");

        if (response.toLowerCase().contains("shop")) {
            enterShop();
        } else if (isGoodbyeResponse(response)) {
            endDialogue();
        } else if (!hasItem) {
            endDialogue();
        } else {
            showDialogueOptions(currentNPC.getDialogueOptions(hasItem));
        }
    }

    private boolean isGoodbyeResponse(String response) {
        String lower = response.toLowerCase();
        return lower.contains("goodbye") || lower.contains("farewell") || lower.contains("walk away");
    }

    // Shop system
    private void enterShop() {
        if (currentNPC == null) {
            endDialogue();
            return;
        }

        inShop = true;
        textArea.appendText("\n=== Shop ===\n");

        Map<String, Integer> inventory = currentNPC.getInventory();
        List<String> items = new ArrayList<>(inventory.keySet());

        if (items.isEmpty()) {
            textArea.appendText("The shop is empty.\n");
            exitShop();
            return;
        }

        dialogueButtons.getChildren().clear();

        for (int i = 0; i < items.size(); i++) {
            final String itemName = items.get(i);
            final int itemIndex = i;
            int price = inventory.get(itemName);

            String priceText = formatPrice(price);
            String buttonText = itemName + " - " + priceText;

            textArea.appendText((i + 1) + ". " + buttonText + "\n");
            dialogueButtons.getChildren().add(
                createDialogueButton(buttonText, () -> handleShopChoice(itemIndex + 1))
            );
        }

        dialogueButtons.getChildren().add(
            createDialogueButton("Exit Shop", () -> handleShopChoice(items.size() + 1))
        );

        dialogueButtons.setVisible(true);
    }

    private String formatPrice(int price) {
        if (price == -1) return "Not found";
        if (price == 0) return "FREE";
        return price + " gold";
    }

    private void handleShopChoice(int choice) {
        if (currentNPC == null) {
            endDialogue();
            return;
        }

        List<String> items = new ArrayList<>(currentNPC.getInventory().keySet());

        if (choice == items.size() + 1) {
            exitShop();
        } else if (choice >= 1 && choice <= items.size()) {
            purchaseItem(items.get(choice - 1));
        }
    }

    private void purchaseItem(String itemName) {
        if (currentNPC == null || itemName == null) {
            textArea.appendText("\nCouldn't purchase item.\n\n");
            return;
        }

        try {
            game.getPlayer().buyToolFromNPC(currentNPC, itemName);
            textArea.appendText("\nYou try to buy: " + itemName + "!\n\n");
            enterShop();
        } catch (Exception e) {
            textArea.appendText("\nCouldn't purchase item.\n\n");
        }
    }

    private void exitShop() {
        if (currentNPC == null) {
            endDialogue();
            return;
        }

        inShop = false;
        textArea.appendText("\nYou leave the shop.\n\n");

        boolean hasItem = game.getPlayer().hasItem(currentNPC.getItemWantedToTalk());
        showDialogueOptions(currentNPC.getDialogueOptions(hasItem));
    }

    private void endDialogue() {
        if (currentNPC != null) {
            textArea.appendText("\nYou end the conversation with " + currentNPC.getName() + ".\n\n");
        }

        currentNPC = null;
        inShop = false;
        dialogueButtons.setVisible(false);
        dialogueButtons.getChildren().clear();
        inputField.setDisable(false);
    }

    // Action result panel management
    private void showActionResult(String command) {
        // Clear previous content
        actionResultArea.clear();

        // Capture output to action panel
        OutputStream actionOut = new OutputStream() {
            private final StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                synchronized (buffer) {
                    char c = (char) b;
                    if (c == '\n') {
                        flush();
                    } else {
                        buffer.append(c);
                    }
                }
            }

            @Override
            public void write(byte[] b, int off, int len) {
                synchronized (buffer) {
                    buffer.append(new String(b, off, len));
                    if (buffer.indexOf("\n") >= 0) {
                        flush();
                    }
                }
            }

            @Override
            public void flush() {
                synchronized (buffer) {
                    if (buffer.length() > 0) {
                        String text = buffer.toString();
                        buffer.setLength(0);
                        Platform.runLater(() -> actionResultArea.appendText(text));
                    }
                }
            }
        };

        // Temporarily redirect output to action panel
        System.setOut(new PrintStream(actionOut, true));

        // Process command
        Command cmd = parser.parseCommandString(command);
        game.processCommand(cmd);

        // Restore main output
        System.setOut(mainOutputStream);

        // Show the panel
        actionResultPane.setVisible(true);
        actionResultPane.setManaged(true);
    }

    private void hideActionResult() {
        actionResultPane.setVisible(false);
        actionResultPane.setManaged(false);
        actionResultArea.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
