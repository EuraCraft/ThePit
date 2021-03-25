package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SniperUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Bow damage (percent)
     */
    private final Map<Integer, Integer> tiersBowDamage;

    public SniperUpgrade() {
        super(UpgradeType.SNIPER, "sniper", ChatColor.LIGHT_PURPLE + "Tireur d'élite", ChatColor.GRAY + "Vous infligez " + ChatColor.RED + "3%" + ChatColor.GRAY + " de dégâts supplémentaires", ChatColor.GRAY + "lorsque vous utilisez votre arc.");
        tiersBowDamage = new HashMap<>();

        tiersBowDamage.put(1, 3);
        setPrice(1, 10.0);
        setLevelRequired(1, Level.ONE);

        tiersBowDamage.put(2, 6);
        setPrice(2, 20.0); // 1500
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
        int tierBowDamage = tiersBowDamage.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.RED + "+" + tierBowDamage + "%";
    }

    @Override
    public short getDyeColor() {
        // yellow
        return (short) 11;
    }

    @Override
    public int getMaxTier() {
        return tiersBowDamage.size();
    }

}
