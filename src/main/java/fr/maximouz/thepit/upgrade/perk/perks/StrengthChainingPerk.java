package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrengthChainingPerk extends Perk {

    private static final int max = 50;
    private final Map<UUID, Integer> damageMultiplier;
    private final Map<UUID, BukkitTask> end;

    public StrengthChainingPerk() {
        super(PerkType.STRENGTH_CHAINING, "Assoiffé de sang", Material.REDSTONE, Level.TEN, 2000,
                "§7Vous infligez §c+8%§7 de §cdégâts§7 pendant",
                "§77 secondes à chaque meurtre",
                "" ,
                "§7Max: " + max + "%"
        );
        damageMultiplier = new HashMap<>();
        end = new HashMap<>();
    }

    private double getMultiplier(Player player) {
        return 1 + damageMultiplier.getOrDefault(player.getUniqueId(), -100) / 100.0;
    }

    @Override
    public void onUnselected(Player player) {
        damageMultiplier.remove(player.getUniqueId());
        end.remove(player.getUniqueId());
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.isCancelled() || event.getDamager().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getDamager();

        if (hasSelected(player)) {

            double multiplier = getMultiplier(player);
            if (multiplier > 1)
                event.setDamage(event.getDamage() * multiplier);

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        damageMultiplier.remove(event.getVictim().getUniqueId());

        Player player = event.getPlayer();

        if (player != null && hasSelected(player)) {

            int multiplier = Math.min(max, 8 + damageMultiplier.getOrDefault(player.getUniqueId(),0));
            System.out.print("strength chaining for " + player.getName() + " = " + multiplier);
            damageMultiplier.put(player.getUniqueId(), multiplier);
            BukkitTask task = end.get(player.getUniqueId());

            if (task != null)
                task.cancel();

            end.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLaterAsynchronously(ThePit.getInstance(), () -> {
                damageMultiplier.remove(player.getUniqueId());
                end.remove(player.getUniqueId());
            }, 20 * 7));

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        damageMultiplier.remove(player.getUniqueId());
        BukkitTask task = end.get(player.getUniqueId());
        if (task != null)
            task.cancel();
        end.remove(player.getUniqueId());
    }


}
