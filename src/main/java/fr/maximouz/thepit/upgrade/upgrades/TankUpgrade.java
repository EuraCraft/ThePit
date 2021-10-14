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

    public TankUpgrade(UpgradeType type) {
        super(type);
        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 1);
        setPrice(1, 450.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 2);
        setPrice(2, 1050.0);
        setLevelRequired(2, Level.ONE);

        tiersMultiplier.put(3, 3);
        setPrice(3, 1500.0);
        setLevelRequired(3, Level.ONE);

        tiersMultiplier.put(4, 4);
        setPrice(4, 2250.0);
        setLevelRequired(4, Level.SEVENTY);

        tiersMultiplier.put(5, 5);
        setPrice(5, 3000.0);
        setLevelRequired(5, Level.SEVENTY);

        /*tiersMultiplier.put(6, 6);
        setPrice(6, 4000.0);
        setLevelRequired(6, Level.SEVENTY);*/

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
        return 1 - (tiersMultiplier.get(tier) / 100.0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.isCancelled() || event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getEntity();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setDamage(getMultiplier(playerTier) * event.getDamage());

        }

    }

}
