package fr.maximouz.thepit.events;

import fr.maximouz.thepit.bank.Bank;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class EarnGoldEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final EarnReason reason;
    private final Bank bank;
    private double amount;

    public EarnGoldEvent(EarnReason reason, Player player, Bank bank, double amount) {
        super(player);
        this.reason = reason;
        this.bank = bank;
        this.amount = amount;
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

    public EarnReason getReason() {
        return reason;
    }

    public Bank getBank() {
        return bank;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
