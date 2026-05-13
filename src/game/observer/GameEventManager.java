package game.observer;

import java.util.ArrayList;
import java.util.List;

public class GameEventManager {

    private List<GameEventListener> listeners;

    public GameEventManager() {
        listeners = new ArrayList<>();
    }

    // Subscribe to events
    public void subscribe(GameEventListener listener) {
        listeners.add(listener);
    }

    // Unsubscribe from events
    public void unsubscribe(GameEventListener listener) {
        listeners.remove(listener);
    }

    // Notify ALL listeners when something happens
    public void notify(String message) {
        for (GameEventListener listener : listeners) {
            listener.onEvent(message);
        }
    }
}