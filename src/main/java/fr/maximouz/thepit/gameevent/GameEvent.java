package fr.maximouz.thepit.gameevent;

import org.bukkit.event.Listener;

public abstract class GameEvent implements IGameEvent, Listener {

    private final GameEventType type;

    public GameEvent(GameEventType type) {
        this.type = type;
    }

    public GameEventType getType() {
        return type;
    }
}
