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
        System.out.println("\n📖 STORY:");
        System.out.println("The Forgotten King's dungeon has awakened.");
        System.out.println("A dragon guards the exit. You must find");
        System.out.println("3 ancient artifacts and 3 magic weapons");
        System.out.println("to stand a chance against it.\n");
        System.out.println("🎯 GOAL:");
        System.out.println("1. Find the Magic Sword → learn Slash");
        System.out.println("2. Find the Magic Staff → learn Fireball");
        System.out.println("3. Find the Dragon Shield → learn Block");
        System.out.println("4. Collect all 3 Forgotten Artifacts");
        System.out.println("5. Enter Boss Chamber and defeat the dragon!");
        System.out.println("\n📋 COMMANDS:");
        System.out.println("  look              - Look around");
        System.out.println("  go [direction]    - Move north/south/east/west");
        System.out.println("  pick up [item]    - Pick up an item");
        System.out.println("  drop [item]       - Drop an item");
        System.out.println("  inventory         - Check inventory");
        System.out.println("  skills            - Check your skills");
        System.out.println("  help              - Show commands again");
        System.out.println("  quit              - Exit game");
        System.out.println("\n===========================================\n");
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
// Check if player entered boss chamber
            if (currentPlayer.getCurrentRoom().getName()
                    .equals("Boss Chamber")) {
                long artifactCount = currentPlayer.getInventory()
                        .stream()
                        .filter(i -> i.getName().equals("Forgotten Artifact"))
                        .count();

                if (artifactCount < 3) {
                    System.out.println("⚠️  You need all 3 artifacts " +
                            "before fighting the dragon!");
                    System.out.println("Artifacts collected: " +
                            artifactCount + "/3");
                } else {
                    // Start combat!
                    Combat combat = new Combat(currentPlayer);
                    boolean won = combat.start();
                    if (won) {
                        System.out.println("\n🏆 CONGRATULATIONS " +
                                currentPlayer.getName() + "!");
                        System.out.println("The dragon is defeated!");
                        System.out.println("The Dungeon of the Forgotten King" +
                                " is free at last!");
                        break;
                    } else {
                        System.out.println("\n💀 You were defeated...");
                        System.out.println("The dragon wins this time.");
                        break;
                    }
                }
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