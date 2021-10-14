package fr.maximouz.thepit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerShootPlayerEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player victim;
    private final double finalDamage;
    private final boolean critical;

    public PlayerShootPlayerEvent(Player player, Player victim, double finalDamage, boolean critical) {
        super(player);
        this.victim = victim;
        this.finalDamage = finalDamage;
        this.critical = critical;
    }

    public Player getVictim() {
        return victim;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public boolean isCritical() {
        return critical;
    }

    public boolean isShootingHimself() {
        return player == victim;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
