package game.core;

import game.factory.RoomFactory;
import game.model.Player;
import game.model.Room;
import game.ui.CommandParser;
import java.util.Scanner;

public class Game {

    private GameWorld world;
    private Scanner scanner;

    public Game() {
        world = GameWorld.getInstance();
        scanner = new Scanner(System.in);
    }

    public void start() {
        printTitle();
        setupWorld();
        setupPlayers();
        gameLoop();
    }

    private void printTitle() {
        System.out.println("===========================================");
        System.out.println("   DUNGEON OF THE FORGOTTEN KING");
        System.out.println("===========================================");
        System.out.println("Find the 3 artifacts and defeat the guardian.");
        System.out.println("Type 'help' for commands.\n");
    }

    private void setupWorld() {
        // Create rooms using Factory pattern
        Room entrance = RoomFactory.createRoom("entrance");
        Room library = RoomFactory.createRoom("library");
        Room armory = RoomFactory.createRoom("armory");
        Room crypt = RoomFactory.createRoom("crypt");
        Room treasure = RoomFactory.createRoom("treasure");
        Room boss = RoomFactory.createRoom("boss");

        // Connect rooms
        entrance.addExit("north", library);
        entrance.addExit("east", armory);
        library.addExit("south", entrance);
        library.addExit("east", boss);
        armory.addExit("west", entrance);
        armory.addExit("north", crypt);
        crypt.addExit("south", armory);
        crypt.addExit("east", treasure);
        treasure.addExit("west", crypt);
        treasure.addExit("north", boss);
        boss.addExit("west", library);
        boss.addExit("south", treasure);

        // Add rooms to world
        world.addRoom(entrance);
        world.addRoom(library);
        world.addRoom(armory);
        world.addRoom(crypt);
        world.addRoom(treasure);
        world.addRoom(boss);

        world.setStartingRoom(entrance);
    }

    private void setupPlayers() {
        System.out.print("How many players? (1 or 2): ");
        int numPlayers = Integer.parseInt(scanner.nextLine().trim());
        numPlayers = Math.min(2, Math.max(1, numPlayers));

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine().trim();
            Player player = new Player(name, world.getStartingRoom());
            world.addPlayer(player);
            // Subscribe player to game events (Observer pattern)
            world.getEventManager().subscribe(player);
        }
        System.out.println("\nAll players start in: " +
                world.getStartingRoom().getName());
    }

    private void gameLoop() {
        // Each player gets a turn
        int currentPlayerIndex = 0;

        while (true) {
            Player currentPlayer = world.getPlayers().get(currentPlayerIndex);
            CommandParser parser = new CommandParser(currentPlayer);

            System.out.println("\n--- " + currentPlayer.getName() + "'s turn ---");
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            String result = parser.parse(input);
            System.out.println(result);

            // Check win condition
            if (checkWin(currentPlayer)) {
                System.out.println("\n🏆 " + currentPlayer.getName() +
                        " has won the game!");
                break;
            }

            // Next player's turn
            currentPlayerIndex =
                    (currentPlayerIndex + 1) % world.getPlayers().size();
        }
        scanner.close();
    }

    private boolean checkWin(Player player) {
        // Win if player has all 3 artifacts and is in boss chamber
        long artifactCount = player.getInventory().stream()
                .filter(i -> i.getName().equals("Forgotten Artifact"))
                .count();
        return artifactCount >= 3 &&
                player.getCurrentRoom().getName().equals("Boss Chamber");
    }

    public static void main(String[] args) {
        new Game().start();
    }
}