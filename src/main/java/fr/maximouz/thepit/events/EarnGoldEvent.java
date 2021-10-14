package fr.maximouz.thepit.events;

import fr.maximouz.thepit.bank.Bank;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

public class EarnGoldEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Bank bank;
    private final BigDecimal amount;

    public EarnGoldEvent(Bank bank, BigDecimal amount) {
        this.bank = bank;
        this.amount = amount;
    }

    public Bank getBank() {
        return bank;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
