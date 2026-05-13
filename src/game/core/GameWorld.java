package game.core;

import game.model.Player;
import game.model.Room;
import java.util.ArrayList;
import java.util.List;
import game.observer.GameEventManager;


public class GameWorld {

    // The one and only instance
    private static GameWorld instance;

    private List<Room> rooms;
    private List<Player> players;
    private Room startingRoom;
    private GameEventManager eventManager;


    // Private constructor - nobody can do "new GameWorld()" from outside
    private GameWorld() {
        rooms = new ArrayList<>();
        players = new ArrayList<>();
        eventManager = new GameEventManager();
    }

    // This is how everyone accesses the one GameWorld
    public static GameWorld getInstance() {
        if (instance == null) {
            instance = new GameWorld();
        }
        return instance;
    }

    public void addRoom(Room room) { rooms.add(room); }
    public void addPlayer(Player player) { players.add(player); }
    public List<Room> getRooms() { return rooms; }
    public List<Player> getPlayers() { return players; }
    public Room getStartingRoom() { return startingRoom; }
    public void setStartingRoom(Room room) { this.startingRoom = room; }
    public GameEventManager getEventManager() { return eventManager; }

}