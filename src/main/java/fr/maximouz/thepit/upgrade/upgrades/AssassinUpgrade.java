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

public class AssassinUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Melee damage multiplier
     */
    private final Map<Integer, Integer> tiersMultiplier;

    public AssassinUpgrade() {
        super(UpgradeType.ASSASSIN, "assassin", "Assassin", ChatColor.GRAY + "Vous infligez " + ChatColor.RED + "1%" + ChatColor.GRAY + " de dégâts supplémentaires", ChatColor.GRAY + "au corps à corps.");
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

        tiersMultiplier.put(6, 6);
        setPrice(6, 4000.0);
        setLevelRequired(6, Level.SEVENTY);

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
        return tiersMultiplier.size();
    }

    public double getMultiplier(int tier) {
        return 1 + (tiersMultiplier.get(tier) / 100.0);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.isCancelled() || event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER || event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;

        Player player = (Player) event.getDamager();

        int playerTier = getTier(player);
        if (playerTier > 0) {

            event.setDamage(getMultiplier(playerTier) * event.getDamage());

        }

    }

}
