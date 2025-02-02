package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class SniperUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersMultiplier;

    public SniperUpgrade(UpgradeType type) {
        super(type);
        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 3);
        setPrice(1, 450.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 6);
        setPrice(2, 1050.0);
        setLevelRequired(2, Level.ONE);

        tiersMultiplier.put(3, 9);
        setPrice(3, 1500.0);
        setLevelRequired(3, Level.ONE);

        tiersMultiplier.put(4, 12);
        setPrice(4, 2250.0);
        setLevelRequired(4, Level.SEVENTY);

        tiersMultiplier.put(5, 15);
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.isCancelled() || event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.ARROW || !(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setDamage(getMultiplier(playerTier) * event.getDamage());

        }

    }

}
