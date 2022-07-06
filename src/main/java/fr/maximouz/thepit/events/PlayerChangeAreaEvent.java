package fr.maximouz.thepit.events;

import fr.maximouz.thepit.area.Area;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerChangeAreaEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Area areaFrom;
    private final Area areaTo;

    public PlayerChangeAreaEvent(Player player, Area areaFrom, Area areaTo) {
        super(player);
        this.areaFrom = areaFrom;
        this.areaTo = areaTo;
    }

    public Area getAreaFrom() {
        return areaFrom;
    }

    public Area getAreaTo() {
        return areaTo;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
