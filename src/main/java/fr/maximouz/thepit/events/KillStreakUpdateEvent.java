package fr.maximouz.thepit.events;

import fr.maximouz.thepit.statistic.PlayerStatistic;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class KillStreakUpdateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final PlayerStatistic playerStatistic;
    private final long newKillStreak;

    public KillStreakUpdateEvent(PlayerStatistic playerStatistic, long newKillStreak) {
        super(playerStatistic.getPlayer());
        this.playerStatistic = playerStatistic;
        this.newKillStreak = newKillStreak;
    }

    public PlayerStatistic getPlayerStatistic() {
        return playerStatistic;
    }

    public long getNewKillStreak() {
        return newKillStreak;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
