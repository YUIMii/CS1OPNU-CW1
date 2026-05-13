package game.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Room currentRoom;
    private List<Item> inventory;
    private int health;

    public Player(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();
        this.health = 100;
    }

    public void moveTo(Room room) {
        this.currentRoom = room;
    }

    public boolean pickUp(Item item) {
        if (item.isPickable()) {
            inventory.add(item);
            currentRoom.removeItem(item.getName());
            return true;
        }
        return false;
    }

    public void dropItem(Item item) {
        inventory.remove(item);
        currentRoom.addItem(item);
    }

    public Item getInventoryItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public String getName() { return name; }
    public Room getCurrentRoom() { return currentRoom; }
    public List<Item> getInventory() { return inventory; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public String getInventoryString() {
        if (inventory.isEmpty()) return "Your inventory is empty.";
        StringBuilder sb = new StringBuilder("Inventory:\n");
        for (Item item : inventory) {
            sb.append("  - ").append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}