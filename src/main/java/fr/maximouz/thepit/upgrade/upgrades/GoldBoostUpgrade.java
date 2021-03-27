package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.EarnGoldEvent;
import fr.maximouz.thepit.events.EarnReason;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;

public class GoldBoostUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersMultiplier;

    public GoldBoostUpgrade() {
        super(UpgradeType.GOLD_BOOST, "goldboost", ChatColor.GOLD + "Boost Gold", ChatColor.GRAY + "Vous gagnez " + ChatColor.YELLOW + "10% de Gold" + ChatColor.GRAY + " supplémentaires", ChatColor.GRAY + "lorsque vous réalisez un meurtre, une", ChatColor.GRAY + "assistance ou que vous en ramassez au sol.");

        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 10);
        setPrice(1, 1000.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 20);
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
        return tiersMultiplier.size();
    }

    @Override
    public String getBonus(Player player) {
        int playerTier = getTier(player);
        int tierMultiplier = tiersMultiplier.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.YELLOW + "+" + tierMultiplier + "% Gold";
    }

    @Override
    public short getDyeColor() {
        // orange
        return (short) 14;
    }

    public double getMultiplier(int tier) {
        return 1 + (tiersMultiplier.get(tier) / 100.0);
    }

    @EventHandler
    public void onEarnGold(EarnGoldEvent event) {

        if (event.getReason() != EarnReason.KILL && event.getReason() != EarnReason.ASSIST && event.getReason() != EarnReason.PICK_UP)
            return;

        Player player = event.getPlayer();
        double amount = event.getAmount();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setAmount(getMultiplier(playerTier) * amount);

        }

    }

}
