Prerequisites:

Java 17+
Maven 3.6+

Verify versions:

bashjava -version
mvn -version

Start:

Compile
bashmvn clean compile

Run Single Player (Console)
bashmvn exec:java -Dexec.mainClass="ZorkGame.game.Main"

Run Single Player (GUI)
bashmvn javafx:run

Run Multiplayer

Server:
bash./run-server.sh
Client (open in separate terminal for each player):
bash./run-client.sh

Specify remote server:
bash./run-client.sh 192.168.1.100 8080

Configure server port and max players:
bash./run-server.sh 9000 4


Game Commands:
 Move using:
          go north | go south | go east | go west
        pick/take <item>    => pick up something in the room
        drop <item>         => drop an item from your inventory
        use <tool> <item>   => use a tool on a raw material
        squeeze <berry>     => create juice if possible
        inventory / inv     => show your items
        recipes             => check potion progress
        Raw materials must be processed with the right tools.
          secateurs => fern, grass
          knife     => lavender
        Combine processed ingredients to craft potions.
        talk <name>         => speak with NPCs
        map                 => show the world map
        save <filename>     => save the game (default: savegame.sav)
        load <filename>     => load a saved game
        NPCs may require specific items before helping you.


Build:

Create JAR
bashmvn clean package -Dmaven.test.skip=true

Run from JAR
bashjava -jar target/my-game-1.0-SNAPSHOT.jar

Multiplayer server from JAR:
bashjava -cp target/my-game-1.0-SNAPSHOT.jar ZorkGame.networking.MultiplayerGameServer

Multiplayer client from JAR:
bashjava -cp target/my-game-1.0-SNAPSHOT.jar ZorkGame.networking.MultiplayerClient


Project Structure:

JavaZork/
├── src/main/java/ZorkGame/
│   ├── game/          Core game logic
│   ├── models/        Game entities
│   ├── commands/      Command parsing
│   ├── GUI/           JavaFX interface
│   ├── networking/    Multiplayer systems
│   ├── threading/     Background tasks
│   ├── enums/         Game constants
│   └── utils/         Utilities
├── src/main/resources/
│   ├── fxml/          UI layouts
│   └── backgrounds/   Background images
├── run-server.sh      Server launcher
├── run-client.sh      Client launcher
└── pom.xml            Maven configuration


Troubleshooting:

IssueSolutionMain class not found Run
mvn clean compile#
GUI fails to startVerify JavaFX configuration in pom.xml
Connection refused => ensure server is running and port 8080 is open
Build errors run
mvn clean install -Dmaven.test.skip=true



mvn test
mvn clean install