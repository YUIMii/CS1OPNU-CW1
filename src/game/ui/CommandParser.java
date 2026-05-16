package game.ui;

import game.model.Item;
import game.model.Player;
import game.model.Room;
import game.core.GameWorld;
import game.model.NPC;

public class CommandParser {

    private Player player;

    public CommandParser(Player player) {
        this.player = player;
    }

    public String parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Please enter a command.";
        }

        String[] parts = input.trim().toLowerCase().split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "go":
                return handleGo(argument);
            case "pick":
                return handlePick(argument);
            case "drop":
                return handleDrop(argument);
            case "look":
                return handleLook();
            case "inventory":
                return player.getInventoryString();
            case "help":
                return handleHelp();
            case "skills":
                return player.getSkillsString();
            case "talk":
                return handleTalk();
            case "trade":
                return handleTrade(argument);
            case "use":
                return handleUse(argument);
            default:
                return "Unknown command. Type 'help' for a list of commands.";
        }
    }

    private String handleGo(String direction) {
        if (direction.isEmpty()) return "Go where?";
        Room nextRoom = player.getCurrentRoom().getExit(direction);
        if (nextRoom == null) {
            return "You can't go that way.";
        }

        // check if room is locked
        if (nextRoom.isLocked()) {
            Item key = player.getInventoryItem(nextRoom.getKeyItemName());
            if (key == null) {
                return "🔒 The " + nextRoom.getName() +
                        " is locked! You need: " + nextRoom.getKeyItemName();
            }
            // unlock it
            nextRoom.unlock();
            player.dropItem(key); // key is used up
            System.out.println("🔑 You used the " + key.getName() +
                    " to unlock " + nextRoom.getName() + "!");
        }

        player.moveTo(nextRoom);
        GameWorld.getInstance().getEventManager()
                .notify(player.getName() + " moved to " + nextRoom.getName());
        return handleLook();
    }

    private String handlePick(String argument) {
        String itemName = argument.replace("up ", "").trim();
        if (itemName.isEmpty()) return "Pick up what?";

        Item item = player.getCurrentRoom().getItems().stream()
                .filter(i -> i.getName().toLowerCase()
                        .contains(itemName.toLowerCase()))
                .findFirst().orElse(null);

        if (item == null) return "There is no " + itemName + " here.";

        if (player.pickUp(item)) {
            GameWorld.getInstance().getEventManager()
                    .notify(player.getName() + " picked up " + item.getName());

            // teach skill if weapon
            String result = "You picked up " + item.getName() + ".";
            // show artifact count if picked up artifact
            if (item.getName().equals("Forgotten Artifact")) {
                long count = player.getInventory().stream()
                        .filter(i -> i.getName().equals("Forgotten Artifact"))
                        .count();
                result += "\n📜 Artifacts collected: " + count + "/3";
                if (count == 3) {
                    result += "\n⚠️  You have all 3 artifacts! " +
                            "Head to the Boss Chamber!";
                }
            }
            if (item.teachesSkill()) {
                player.learnSkill(item.getSkill());
                result += "\nYou learned: " + item.getSkill().getName() + "!";
            }
            return result;
        }
        return "You can't pick that up.";
    }

    private String handleDrop(String itemName) {
        if (itemName.isEmpty()) return "Drop what?";
        Item item = player.getInventoryItem(itemName);
        if (item == null) return "You don't have " + itemName + ".";
        player.dropItem(item);
        GameWorld.getInstance().getEventManager()
                .notify(player.getName() + " dropped " + item.getName());
        return "You dropped " + item.getName() + ".";
    }

    private String handleLook() {
        Room room = player.getCurrentRoom();
        StringBuilder sb = new StringBuilder();
        sb.append("\n❤️  Health: ").append(player.getHealth()).append("/100\n");
        sb.append("=== ").append(room.getName()).append(" ===\n");
        sb.append(room.getDescription()).append("\n");

        if (room.hasNPC()) {
            sb.append("👤 ").append(room.getNPC().getName())
                    .append(" is here.\n");
        }
        if (!room.getItems().isEmpty()) {
            sb.append("Items here: ");
            room.getItems().forEach(i ->
                    sb.append(i.getName()).append(" "));
            sb.append("\n");
        }
        sb.append(room.getExitsString());
        return sb.toString();
    }

    private String handleTalk() {
        Room room = player.getCurrentRoom();
        if (!room.hasNPC()) {
            return "There is nobody here to talk to.";
        }
        NPC npc = room.getNPC();
        return npc.interact();
    }

    private String handleTrade(String argument) {
        Room room = player.getCurrentRoom();
        if (!room.hasNPC()) return "There is nobody here to trade with.";

        NPC npc = room.getNPC();
        if (argument.isEmpty()) return "Trade what? Type 'talk' to see trades.";

        // find item in inventory
        Item playerItem = player.getInventoryItem(argument);
        if (playerItem == null) {
            return "You don't have " + argument + " in your inventory.";
        }

        // check if NPC accepts this item
        String receiveItemName = npc.getTrades()
                .get(playerItem.getName().toLowerCase());
        if (receiveItemName == null) {
            return npc.getName() + " doesn't want that item.";
        }

        // do the trade
        player.dropItem(playerItem);
        Item receivedItem = new Item(receiveItemName,
                "Traded from " + npc.getName(), true);
        player.pickUp(receivedItem);

        GameWorld.getInstance().getEventManager()
                .notify(player.getName() + " traded with " + npc.getName());

        return "✅ Trade complete! You gave " + playerItem.getName() +
                " and received " + receiveItemName + "!";
    }

    private String handleUse(String argument) {
        if (argument.isEmpty()) return "Use what?";

        Item item = player.getInventoryItem(argument);
        if (item == null) return "You don't have " + argument + ".";

        if (item.getName().equalsIgnoreCase("Health Potion")) {
            int oldHealth = player.getHealth();
            player.setHealth(Math.min(100, oldHealth + 30));
            player.dropItem(item);
            player.getCurrentRoom().removeItem("Health Potion");
            return "🧪 You drank the Health Potion! +" +
                    (player.getHealth() - oldHealth) + " HP. " +
                    "Health: " + player.getHealth() + "/100";
        }
        return "You can't use that.";
    }

    private String handleHelp() {
        return "\nAvailable commands:\n" +
                "  go [direction]    - Move (north, south, east, west)\n" +
                "  look              - Look around the room\n" +
                "  pick up [item]    - Pick up an item\n" +
                "  drop [item]       - Drop an item\n" +
                "  inventory         - Check your inventory\n" +
                "  help              - Show this list\n" +
                "  skills           - Show your learned skills\n" +
                "  talk              - Talk to NPC in the room\n" +
                "  trade [item]      - Trade an item with NPC\n" +
                "  use [item]        - Use an item (e.g. Health Potion)\n" +
                "  quit              - Exit the game\n";
    }
}