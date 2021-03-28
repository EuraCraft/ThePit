package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class StrengthChainingPerk extends Perk {

    private static final int max = 50;
    private final Map<Player, Integer> damageMultiplier;
    private final Map<Player, BukkitTask> end;

    public StrengthChainingPerk() {
        super(PerkType.STRENGTH_CHAINING, "Assoiffé de sang", Material.REDSTONE, Level.TEN, 2000, ChatColor.GRAY + "Vous infligez " + ChatColor.RED + "+8% " + ChatColor.GRAY + "de " + ChatColor.RED + "dégâts " + ChatColor.GRAY + "au" + ChatColor.RED + " corps à corps ", ChatColor.GRAY + "pendant 7 secondes lorsque vous réalisez", ChatColor.GRAY + "un meurtre.", "" , ChatColor.GRAY + "Max: "+ ChatColor.ITALIC + max + "%");
        damageMultiplier = new HashMap<>();
        end = new HashMap<>();
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    private double getMultiplier(Player player) {
        return 1 + damageMultiplier.getOrDefault(player, 1) / 100.0;
    }

    @Override
    public void onUnselect(Player player) {
        damageMultiplier.remove(player);
        end.remove(player);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.isCancelled() || event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER || event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            return;

        Player player = (Player) event.getDamager();

        if (hasSelected(player)) {

            event.setDamage(event.getDamage() * getMultiplier(player));

        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        damageMultiplier.remove(player);

        if (player.getKiller() != null) {

            Player killer = player.getKiller();

            if (hasSelected(killer)) {

                damageMultiplier.put(killer, Math.min(max, 1 + damageMultiplier.getOrDefault(killer,1)));
                BukkitTask task = end.get(killer);
                if (task != null)
                    task.cancel();
                end.put(killer, Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                    damageMultiplier.remove(killer);
                    end.remove(killer);
                }, 20 * 7));

            }

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        damageMultiplier.remove(player);
        BukkitTask task = end.get(player);
        if (task != null)
            task.cancel();
        end.remove(player);
    }


}
