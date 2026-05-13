package game.model;

public class Item {
    private String name;
    private String description;
    private boolean pickable;
    private Skill skill;

    public Item(String name, String description, boolean pickable) {
        this.name = name;
        this.description = description;
        this.pickable = pickable;
        this.skill = null;
    }

    public Item(String name, String description, boolean pickable, Skill skill) {
        this.name = name;
        this.description = description;
        this.pickable = pickable;
        this.skill = skill;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPickable() { return pickable; }
    public Skill getSkill() { return skill; }
    public boolean teachesSkill() { return skill != null; }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}