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

import java.util.HashMap;
import java.util.Map;

public class TankUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Damage reduction multiplier
     */
    private final Map<Integer, Integer> tiersMultiplier;

    public TankUpgrade() {
        super(UpgradeType.TANK, "tank", ChatColor.BLUE + "Tank", ChatColor.GRAY + "Vous recevez " + ChatColor.DARK_AQUA + "1%" + ChatColor.GRAY + " de dégâts " + ChatColor.DARK_AQUA + "en moins", ChatColor.GRAY + "lorsqu'un joueur vous attaque.");
        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 1);
        setPrice(1, 10.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 2);
        setPrice(2, 1500.0);
        setLevelRequired(2, Level.TWO);

        tiersMultiplier.put(3, 3);
        setPrice(3, 2250.0); // 1500
        setLevelRequired(3, Level.TWO);

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
        return ChatColor.DARK_AQUA + "-" + tierBowDamage + "%";
    }

    @Override
    public short getDyeColor() {
        // cyan
        return (short) 6;
    }

    @Override
    public int getMaxTier() {
        return tiersMultiplier.size();
    }

    public double getMultiplier(int tier) {
        return tiersMultiplier.get(tier) / 100.0;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getEntity();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setDamage(getMultiplier(playerTier) * event.getDamage());

        }

    }

}
