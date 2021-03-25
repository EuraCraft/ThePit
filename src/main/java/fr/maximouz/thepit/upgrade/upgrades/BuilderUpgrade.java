package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BuilderUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Block life time
     */
    private final Map<Integer, Integer> tiersBlockLifeTime;

    public BuilderUpgrade() {
        super(UpgradeType.BUILDER, "builder", ChatColor.DARK_GREEN + "Constructeur", ChatColor.GRAY + "Vos blocs restent en vie " + ChatColor.GREEN + "60%" + ChatColor.GRAY + " plus", ChatColor.GRAY + "longtemps qu'avant.");
        tiersBlockLifeTime = new HashMap<>();

        tiersBlockLifeTime.put(1, 60);
        setPrice(1, 1750);
        setLevelRequired(1, Level.ONE);

        tiersBlockLifeTime.put(2, 120);
        setPrice(2, 2750); // 1500
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
        int tierBlockLifeTime = tiersBlockLifeTime.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.GREEN + "+" + tierBlockLifeTime + "%";
    }

    @Override
    public short getDyeColor() {
        // white
        return (short) 15;
    }

    @Override
    public int getMaxTier() {
        return tiersBlockLifeTime.size();
    }
}
