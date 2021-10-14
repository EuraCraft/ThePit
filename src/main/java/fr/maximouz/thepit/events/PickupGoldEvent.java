package fr.maximouz.thepit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.math.BigDecimal;

public class PickupGoldEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private BigDecimal goldReward;

    public PickupGoldEvent(Player player, BigDecimal goldReward) {
        super(player);
        cancelled = false;

        this.goldReward = goldReward;
    }

    public BigDecimal getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(BigDecimal goldReward) {
        this.goldReward = goldReward;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
