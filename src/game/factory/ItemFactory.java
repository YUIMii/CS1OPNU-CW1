package game.factory;

import game.model.Item;
import game.model.Skill;

public class ItemFactory {

    public static Item createItem(String type) {
        switch (type.toLowerCase()) {
            case "sword":
                return new Item("Magic Sword",
                        "A glowing blade humming with ancient power.",
                        true,
                        new Skill("Slash", "A powerful sword strike", 40));
            case "shield":
                return new Item("Dragon Shield",
                        "A shield forged from dragon scales.",
                        true,
                        new Skill("Block", "Reduces incoming damage", 20));
            case "potion":
                return new Item("Health Potion",
                        "A bubbling red liquid that restores health.", true);
            case "staff":
                return new Item("Magic Staff",
                        "A twisted staff crackling with lightning.",
                        true,
                        new Skill("Fireball", "A blazing ball of fire", 50));
            case "key":
                return new Item("Ancient Key",
                        "A rusty key engraved with a skull.", true);
            case "artifact":
                return new Item("Forgotten Artifact",
                        "A mysterious relic of the Forgotten King.", true);
            case "altar":
                return new Item("Stone Altar",
                        "A massive stone altar. It cannot be moved.", false);
            default:
                throw new IllegalArgumentException("Unknown item type: " + type);
        }
    }
}
