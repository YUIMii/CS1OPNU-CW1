import game.core.GameWorld;
import game.factory.ItemFactory;
import game.factory.RoomFactory;
import game.model.Item;
import game.model.Room;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameWorldTest {

    // Test 1: GameWorld is a Singleton
    @Test
    void gameWorldIsSingleton() {
        GameWorld world1 = GameWorld.getInstance();
        GameWorld world2 = GameWorld.getInstance();
        assertSame(world1, world2); // must be exact same object
    }

    // Test 2: RoomFactory creates correct room
    @Test
    void roomFactoryCreatesArmory() {
        Room armory = RoomFactory.createRoom("armory");
        assertEquals("Armory", armory.getName());
    }

    // Test 3: Room has correct items
    @Test
    void armoryHasSwordAndShield() {
        Room armory = RoomFactory.createRoom("armory");
        assertEquals(2, armory.getItems().size());
    }

    // Test 4: ItemFactory creates correct item
    @Test
    void itemFactoryCreatesSword() {
        Item sword = ItemFactory.createItem("sword");
        assertEquals("Magic Sword", sword.getName());
        assertTrue(sword.isPickable());
    }

    // Test 5: Altar is not pickable
    @Test
    void altarIsNotPickable() {
        Item altar = ItemFactory.createItem("altar");
        assertFalse(altar.isPickable());
    }

    // Test 6: Room exits work correctly
    @Test
    void roomExitsWorkCorrectly() {
        Room entrance = new Room("Entrance", "A hall.");
        Room library = new Room("Library", "A library.");
        entrance.addExit("north", library);
        assertEquals(library, entrance.getExit("north"));
    }

    // Test 7: Invalid exit returns null
    @Test
    void invalidExitReturnsNull() {
        Room room = new Room("Room", "A room.");
        assertNull(room.getExit("north"));
    }

    // Test 8: Unknown item type throws exception
    @Test
    void unknownItemTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ItemFactory.createItem("banana");
        });
    }
}