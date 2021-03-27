package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.ObsidianPlaceEvent;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class BuilderUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Block life time multiplier
     */
    private final Map<Integer, Integer> tiersMultiplier;

    public BuilderUpgrade() {
        super(UpgradeType.BUILDER, "builder", ChatColor.DARK_GREEN + "Constructeur", ChatColor.GRAY + "Vos blocs restent en vie " + ChatColor.GREEN + "60%" + ChatColor.GRAY + " plus", ChatColor.GRAY + "longtemps qu'avant.");
        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 60);
        setPrice(1, 1750);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 120);
        setPrice(2, 2750);
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
    public String getBonus(Player player) {
        int playerTier = getTier(player);
        int tierBlockLifeTime = tiersMultiplier.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.GREEN + "+" + tierBlockLifeTime + "%";
    }

    @Override
    public short getDyeColor() {
        // white
        return (short) 15;
    }

    @Override
    public int getMaxTier() {
        return tiersMultiplier.size();
    }

    public double getTierMultiplier(int tier) {
        return 1 + (tiersMultiplier.get(tier) / 100.0);
    }

    @EventHandler
    public void onObsiPlace(ObsidianPlaceEvent event) {

        Player player = event.getPlayer();
        int playerTier = getTier(player);

        if (playerTier > 0) {

            event.setLifeTime(Format.roundToInt(getTierMultiplier(playerTier) * event.getLifeTime()));

        }

    }
}
