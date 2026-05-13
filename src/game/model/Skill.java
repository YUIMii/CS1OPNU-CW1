package game.model;

public class Skill {
    private String name;
    private String description;
    private int damage;

    public Skill(String name, String description, int damage) {
        this.name = name;
        this.description = description;
        this.damage = damage;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDamage() { return damage; }

    @Override
    public String toString() {
        return name + " (damage: " + damage + ") - " + description;
    }
}