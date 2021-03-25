package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class GoldBoostUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersGold;

    public GoldBoostUpgrade() {
        super(UpgradeType.GOLD_BOOST, "goldboost", ChatColor.GOLD + "Boost Gold", ChatColor.GRAY + "Vous gagnez " + ChatColor.YELLOW + "10% de Gold" + ChatColor.GRAY + " supplémentaires", ChatColor.GRAY + "lorsque vous réalisez un meurtre, une", ChatColor.GRAY + "assistance ou que vous en ramassez au sol.");

        tiersGold = new HashMap<>();

        tiersGold.put(1, 10);
        setPrice(1, 10.0);
        setLevelRequired(1, Level.ONE);

        tiersGold.put(2, 20);
        setPrice(2, 10000.0);
        setLevelRequired(2, Level.TWO);
    }

    @Override
    public void load(Player player) {
        setTier(player, 0);
    }

    @Override
    public void save(Player player) {
        removeTier(player);
    }

    @Override
    public int getMaxTier() {
        return tiersGold.size();
    }

    @Override
    public String getBonus(Player player) {
        int playerTier = getTier(player);
        int tierGold = tiersGold.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.YELLOW + "+" + tierGold + "% Gold";
    }

    @Override
    public short getDyeColor() {
        // orange
        return (short) 14;
    }
}
