package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TankUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Damage reduction (percent)
     */
    private final Map<Integer, Integer> tiersDamageReduction;

    public TankUpgrade() {
        super(UpgradeType.TANK, "tank", ChatColor.BLUE + "Tank", ChatColor.GRAY + "Vous recevez " + ChatColor.DARK_AQUA + "1%" + ChatColor.GRAY + " de dégâts " + ChatColor.DARK_AQUA + "en moins", ChatColor.GRAY + "lorsqu'un joueur vous attaque.");
        tiersDamageReduction = new HashMap<>();

        tiersDamageReduction.put(1, 3);
        setPrice(1, 10.0);
        setLevelRequired(1, Level.ONE);

        tiersDamageReduction.put(2, 6);
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
        int tierBowDamage = tiersDamageReduction.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.DARK_AQUA + "-" + tierBowDamage + "%";
    }

    @Override
    public short getDyeColor() {
        // cyan
        return (short) 6;
    }

    @Override
    public int getMaxTier() {
        return tiersDamageReduction.size();
    }

}
