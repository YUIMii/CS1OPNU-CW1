package game.factory;

import game.model.Item;
import game.model.Room;

public class RoomFactory {

    public static Room createRoom(String type) {
        Room room;
        switch (type.toLowerCase()) {
            case "entrance":
                room = new Room("Entrance Hall",
                        "You stand in a grand hall. Torches flicker on the walls. " +
                                "The air smells of dust and danger.");
                room.addItem(ItemFactory.createItem("potion"));
                return room;

            case "library":
                room = new Room("Ancient Library",
                        "Shelves of crumbling books line the walls. " +
                                "Something rustles in the shadows.");
                room.addItem(ItemFactory.createItem("staff"));
                room.addItem(ItemFactory.createItem("artifact"));
                return room;

            case "armory":
                room = new Room("Armory",
                        "Weapons of fallen warriors hang on the walls. " +
                                "The floor is stained with old blood.");
                room.addItem(ItemFactory.createItem("sword"));
                room.addItem(ItemFactory.createItem("shield"));
                return room;

            case "crypt":
                room = new Room("The Crypt",
                        "Coffins line the walls. A cold wind blows " +
                                "from somewhere deeper in the dungeon.");
                room.addItem(ItemFactory.createItem("artifact"));
                room.addItem(ItemFactory.createItem("key"));
                return room;

            case "treasure":
                room = new Room("Treasure Room",
                        "Gold and jewels glitter everywhere. " +
                                "But something feels wrong...");
                room.addItem(ItemFactory.createItem("artifact"));
                room.addItem(ItemFactory.createItem("altar"));
                return room;

            case "boss":
                room = new Room("Boss Chamber",
                        "A massive dragon raises its head as you enter. " +
                                "Its eyes glow with ancient fury. " +
                                "THIS IS THE END.");
                return room;

            default:
                throw new IllegalArgumentException("Unknown room type: " + type);
        }
    }
}
