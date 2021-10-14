package fr.maximouz.thepit.events;

import fr.maximouz.thepit.bank.Bank;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.math.BigDecimal;

public class EarnExperienceEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Bank bank;
    private final BigDecimal amount;
    private final int levelPassed;

    public EarnExperienceEvent(Bank bank, BigDecimal amount, int levelPassed) {
        this.bank = bank;
        this.amount = amount;
        this.levelPassed = levelPassed;
    }

    public Bank getBank() {
        return bank;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getLevelPassed() {
        return levelPassed;
    }

    public Player getPlayer() {
        return bank.getPlayer();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
