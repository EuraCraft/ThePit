package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.EarnXpEvent;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;

public class XpBoostUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersMultiplier;

    public XpBoostUpgrade() {
        super(UpgradeType.XP_BOOST, "xpboost", "Boost XP", ChatColor.GRAY + "Vous gagnez " + ChatColor.AQUA + "10% d'XP" + ChatColor.GRAY + " supplémentaires", ChatColor.GRAY + "lors de toutes les situations.");

        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 10);
        setPrice(1, 1000.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 20);
        setPrice(2, 2500.0);
        setLevelRequired(2, Level.ONE);

        tiersMultiplier.put(3, 30);
        setPrice(3, 5000.0);
        setLevelRequired(3, Level.ONE);

        tiersMultiplier.put(4, 40);
        setPrice(4, 10000.0);
        setLevelRequired(4, Level.EIGHTY);

        tiersMultiplier.put(5, 50);
        setPrice(5, 25000.0);
        setLevelRequired(5, Level.EIGHTY);

        tiersMultiplier.put(6, 60);
        setPrice(6, 40000.0);
        setLevelRequired(6, Level.EIGHTY);
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
        int tierXp = tiersMultiplier.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.AQUA + "+" + tierXp + "% XP";
    }

    @Override
    public short getDyeColor() {
        // light blue
        return (short) 12;
    }

    public double getMultiplier(int tier) {
        return 1 + tiersMultiplier.get(tier) / 100.0;
    }

    @EventHandler
    public void onEarnXp(EarnXpEvent event) {

        Player player = event.getPlayer();
        double amount = event.getAmount();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setAmount(getMultiplier(playerTier) * amount);

        }

    }

}
