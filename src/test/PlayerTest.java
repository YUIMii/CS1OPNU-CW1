import game.model.Item;
import game.model.Player;
import game.model.Room;
import game.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;
    private Room room;
    private Item sword;
    private Item altar;

    // This runs BEFORE every single test
    // Sets up fresh objects each time
    @BeforeEach
    void setUp() {
        room = new Room("Test Room", "A plain room.");
        sword = new Item("Magic Sword", "A glowing blade.", true,
                new Skill("Slash", "A sword strike", 40));
        altar = new Item("Stone Altar", "A heavy altar.", false);
        room.addItem(sword);
        room.addItem(altar);
        player = new Player("TestPlayer", room);
    }

    // Test 1: Player starts in the right room
    @Test
    void playerStartsInCorrectRoom() {
        assertEquals("Test Room", player.getCurrentRoom().getName());
    }

    // Test 2: Player starts with empty inventory
    @Test
    void playerStartsWithEmptyInventory() {
        assertTrue(player.getInventory().isEmpty());
    }

    // Test 3: Player can pick up a pickable item
    @Test
    void playerCanPickUpItem() {
        boolean result = player.pickUp(sword);
        assertTrue(result); // pickUp should return true
        assertEquals(1, player.getInventory().size()); // inventory has 1 item
    }

    // Test 4: Item is removed from room after pickup
    @Test
    void itemRemovedFromRoomAfterPickup() {
        player.pickUp(sword);
        assertFalse(room.getItems().contains(sword));
    }

    // Test 5: Player cannot pick up non-pickable item
    @Test
    void playerCannotPickUpNonPickableItem() {
        boolean result = player.pickUp(altar);
        assertFalse(result); // should return false
        assertTrue(player.getInventory().isEmpty()); // inventory still empty
    }

    // Test 6: Player learns skill from weapon
    @Test
    void playerLearnsSkillFromWeapon() {
        player.pickUp(sword);
        player.learnSkill(sword.getSkill());
        assertTrue(player.hasSkill("Slash"));
    }

    // Test 7: Player starts with 100 health
    @Test
    void playerStartsWithFullHealth() {
        assertEquals(100, player.getHealth());
    }

    // Test 8: Player can drop item
    @Test
    void playerCanDropItem() {
        player.pickUp(sword);
        player.dropItem(sword);
        assertTrue(player.getInventory().isEmpty());
        assertTrue(room.getItems().contains(sword));
    }
}