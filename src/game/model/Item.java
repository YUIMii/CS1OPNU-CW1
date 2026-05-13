package game.model;

public class Item {
    private String name;
    private String description;
    private boolean pickable;

    public Item(String name, String description, boolean pickable) {
        this.name = name;
        this.description = description;
        this.pickable = pickable;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPickable() { return pickable; }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}