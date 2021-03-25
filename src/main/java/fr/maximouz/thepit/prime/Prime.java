package fr.maximouz.thepit.prime;

import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Prime {

    private final Player player;
    private double gold;

    public Prime(Player player, double defaultGold) {
        this.player = player;
        this.gold = defaultGold;
    }

    public Player getPlayer() {
        return player;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = BigDecimal.valueOf(gold).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    public void bumpGold(double amount) {
        this.gold += BigDecimal.valueOf(amount).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
