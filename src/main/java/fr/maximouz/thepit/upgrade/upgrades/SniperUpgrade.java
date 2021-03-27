package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

public class SniperUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Bow damage multiplier
     */
    private final Map<Integer, Integer> tiersMultiplier;

    public SniperUpgrade() {
        super(UpgradeType.SNIPER, "sniper", ChatColor.LIGHT_PURPLE + "Tireur d'élite", ChatColor.GRAY + "Vous infligez " + ChatColor.RED + "3%" + ChatColor.GRAY + " de dégâts supplémentaires", ChatColor.GRAY + "lorsque vous utilisez votre arc.");
        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 3);
        setPrice(1, 450.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 6);
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
        int tierBowDamage = tiersMultiplier.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.RED + "+" + tierBowDamage + "%";
    }

    @Override
    public short getDyeColor() {
        // yellow
        return (short) 11;
    }

    @Override
    public int getMaxTier() {
        return tiersMultiplier.size();
    }

    public double getMultiplier(int tier) {
        return 1 + (tiersMultiplier.get(tier) / 100.0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER || event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
            return;

        Player player = (Player) event.getDamager();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setDamage(getMultiplier(playerTier) * event.getDamage());

        }

    }

}
