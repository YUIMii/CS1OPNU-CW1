package game.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private String description;
    private Map<String, Room> exits;
    private List<Item> items;
    private NPC npc;
    private boolean isLocked;
    private String keyItemName;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.exits = new HashMap<>();
        items = new ArrayList<>();
        isLocked = false;
        keyItemName = null;
    }

    public void addExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public String getExitsString() {
        return "Exits: " + exits.keySet().toString();
    }
    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                items.remove(item);
                return item;
            }
        }
        return null;
    }

    public List<Item> getItems() { return items; }
    public void setNPC(NPC npc) { this.npc = npc; }
    public NPC getNPC() { return npc; }
    public boolean hasNPC() { return npc != null; }
    public void setLocked(boolean locked, String keyItemName) {
        this.isLocked = locked;
        this.keyItemName = keyItemName;
    }

    public boolean isLocked() { return isLocked; }
    public String getKeyItemName() { return keyItemName; }

    public void unlock() { this.isLocked = false; }

}