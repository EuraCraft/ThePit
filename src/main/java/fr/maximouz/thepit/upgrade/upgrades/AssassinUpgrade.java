package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AssassinUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Melee damage increasing amount
     */
    private final Map<Integer, Integer> tiersMeleeDamage;

    public AssassinUpgrade() {
        super(UpgradeType.ASSASSIN, "assassin", ChatColor.LIGHT_PURPLE + "Assassin", ChatColor.GRAY + "Vous infligez " + ChatColor.RED + "1%" + ChatColor.GRAY + " de dégâts supplémentaires", ChatColor.GRAY + "au corps à corps.");
        tiersMeleeDamage = new HashMap<>();

        tiersMeleeDamage.put(1, 1);
        setPrice(1, 10.0);
        setLevelRequired(1, Level.ONE);

        tiersMeleeDamage.put(2, 1);
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
        return ChatColor.RED + "+1%";
    }

    @Override
    public short getDyeColor() {
        // Rouge
        return (short) 1;
    }

    @Override
    public int getMaxTier() {
        return tiersMeleeDamage.size();
    }
}
