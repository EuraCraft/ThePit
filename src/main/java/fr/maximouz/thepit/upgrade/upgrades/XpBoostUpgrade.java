package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerAssistEvent;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;
import java.util.*;

public class XpBoostUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersMultiplier;

    public XpBoostUpgrade(UpgradeType type) {
        super(type);

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

        /*tiersMultiplier.put(6, 60);
        setPrice(6, 40000.0);
        setLevelRequired(6, Level.EIGHTY);*/
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
    public void onAssist(PlayerAssistEvent event) {

        Player player = event.getPlayer();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setExperienceReward(event.getExperienceReward().multiply(BigDecimal.valueOf(getMultiplier(playerTier))));

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player == null)
            return;

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setExperienceReward(event.getExperienceReward().multiply(BigDecimal.valueOf(getMultiplier(playerTier))));

        }

    }

}
