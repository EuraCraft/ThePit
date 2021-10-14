package fr.maximouz.thepit.prime;

import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Prime {

    private final Player player;
    private BigDecimal gold;

    public Prime(Player player, BigDecimal defaultGold) {
        this.player = player;
        this.gold = defaultGold;
    }

    public Player getPlayer() {
        return player;
    }

    public BigDecimal getGold() {
        return gold;
    }

    public void setGold(BigDecimal gold) {
        this.gold = gold;
    }

    public void bumpGold(BigDecimal amount) {
        gold = gold.add(amount);
    }
}
