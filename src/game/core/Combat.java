package game.core;

import game.model.Player;
import game.model.Skill;
import java.util.Random;
import java.util.Scanner;

public class Combat {

    private Player player;
    private int dragonHealth;
    private int playerHealth;
    private Random random;
    private Scanner scanner;

    // Dragon attacks list
    private String[] dragonAttacks = {
            "The dragon breathes fire at you!",
            "The dragon slashes with its claws!",
            "The dragon smashes you with its tail!",
            "The dragon roars and stuns you!"
    };

    public Combat(Player player) {
        this.player = player;
        this.dragonHealth = 150;
        this.playerHealth = player.getHealth();
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

    public boolean start() {
        printIntro();

        // Check if player has all 3 skills
        boolean hasSlash = player.hasSkill("Slash");
        boolean hasFireball = player.hasSkill("Fireball");
        boolean hasBlock = player.hasSkill("Block");

        if (!hasSlash && !hasFireball && !hasBlock) {
            System.out.println("⚠️  You have no skills! Find weapons first!");
            System.out.println("You flee from the dragon...");
            return false;
        }

        // Combat loop
        while (dragonHealth > 0 && playerHealth > 0) {
            printStatus();
            playerTurn(hasSlash, hasFireball, hasBlock);

            if (dragonHealth <= 0) break;

            dragonTurn(hasBlock);
        }

        return dragonHealth <= 0;
    }

    private void printIntro() {
        System.out.println("\n🐉 ==========================================");
        System.out.println("   THE FORGOTTEN KING'S GUARDIAN AWAKENS!");
        System.out.println("==========================================");
        System.out.println("A massive dragon rises before you!");
        System.out.println("Its scales shimmer with ancient magic.");
        System.out.println("FIGHT!\n");
    }

    private void printStatus() {
        System.out.println("\n--- BATTLE STATUS ---");
        System.out.println("Your HP:     " + playerHealth);
        System.out.println("Dragon HP:   " + dragonHealth);
        System.out.println("---------------------");
    }

    private void playerTurn(boolean hasSlash,
                            boolean hasFireball,
                            boolean hasBlock) {
        System.out.println("\nYour skills:");
        if (hasSlash) System.out.println("  1. Slash     (40 damage)");
        if (hasFireball) System.out.println("  2. Fireball  (50 damage)");
        if (hasBlock) System.out.println("  3. Block     (defend + 20 damage)");
        System.out.print("\nChoose skill (type name): ");

        String input = scanner.nextLine().trim().toLowerCase();
        int damage = 0;

        switch (input) {
            case "slash":
                if (hasSlash) {
                    damage = 40;
                    System.out.println("⚔️  You slash the dragon for " +
                            damage + " damage!");
                } else {
                    System.out.println("You don't know Slash!");
                }
                break;
            case "fireball":
                if (hasFireball) {
                    damage = 50;
                    System.out.println("🔥 You hurl a fireball for " +
                            damage + " damage!");
                } else {
                    System.out.println("You don't know Fireball!");
                }
                break;
            case "block":
                if (hasBlock) {
                    damage = 20;
                    playerHealth = Math.min(100, playerHealth + 15);
                    System.out.println("🛡️  You block and counter for " +
                            damage + " damage! (+15 HP recovered)");
                } else {
                    System.out.println("You don't know Block!");
                }
                break;
            default:
                System.out.println("Unknown skill! You lose your turn.");
        }

        dragonHealth -= damage;
    }

    private void dragonTurn(boolean playerHasBlock) {
        int dragonDamage = 25 + random.nextInt(20);

        // Block skill reduces dragon damage
        if (playerHasBlock) {
            dragonDamage = dragonDamage / 2;
        }

        String attack = dragonAttacks[random.nextInt(dragonAttacks.length)];
        System.out.println("\n🐉 " + attack);
        System.out.println("Dragon deals " + dragonDamage + " damage to you!");
        playerHealth -= dragonDamage;
    }
}