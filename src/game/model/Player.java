package game.model;

import java.util.ArrayList;
import java.util.List;
import game.observer.GameEventListener;
import java.util.List;

public class Player implements GameEventListener {
    private String name;
    private Room currentRoom;
    private List<Item> inventory;
    private int health;
    private List<Skill> skills;

    public Player(String name, Room startingRoom) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();
        this.health = 100;
        skills = new ArrayList<>();
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
    public void learnSkill(Skill skill) {
        skills.add(skill);
        System.out.println("⚔️  " + name + " learned: " + skill.getName() + "!");
    }

    public List<Skill> getSkills() { return skills; }

    public boolean hasSkill(String skillName) {
        return skills.stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(skillName));
    }

    public String getSkillsString() {
        if (skills.isEmpty()) return "No skills yet. Find weapons!";
        StringBuilder sb = new StringBuilder("Skills:\n");
        for (Skill skill : skills) {
            sb.append("  ⚔️  ").append(skill.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void onEvent(String message) {
        System.out.println("[" + name + " received]: " + message);
    }
}
