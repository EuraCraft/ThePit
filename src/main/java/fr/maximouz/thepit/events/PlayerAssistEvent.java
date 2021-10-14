package fr.maximouz.thepit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.math.BigDecimal;

public class PlayerAssistEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final Player victim;
    private BigDecimal experienceReward;
    private BigDecimal goldReward;

    public PlayerAssistEvent(Player player, Player victim, BigDecimal experienceReward, BigDecimal goldReward) {
        super(player);
        this.victim = victim;
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
    }

    public Player getVictim() {
        return victim;
    }

    public BigDecimal getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(BigDecimal experienceReward) {
        this.experienceReward = experienceReward;
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
