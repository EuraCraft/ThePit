package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.items.PerkItem;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class FeastKillStreakPerk extends KillStreakPerk {

    private final Map<UUID, Integer> triggered;

    public FeastKillStreakPerk(KillStreakPerkType type) {
        super(type);
        triggered = new HashMap<>();
    }

    @Override
    public void onUnselected(Player player) {
        player.getInventory().remove(Material.COOKED_MUTTON);
    }

    @Override
    public void trigger(Player player) {

        ItemStack item = new PerkItem(Material.COOKED_BEEF, 1)
                .setName("§6Steak Royal")
                .setLore("§c+20% de dégâts (10s)", "§fRapidié I (10s)", "§9Résistance I (10s)")
                .build();

        player.getInventory().addItem(item);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!hasSelected(player))
            return;

        ItemStack item = event.getItem();

        if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals("§6Steak Royal")) {

            player.playSound(player.getLocation(), Sound.EAT, 1f, 1f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 10, 0));

            if (triggered.containsKey(player.getUniqueId()))
                Bukkit.getScheduler().cancelTask(triggered.get(player.getUniqueId()));

            triggered.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> triggered.remove(player.getUniqueId()), 20 * 10).getTaskId());

            if (item.getAmount() > 1)
                item.setAmount(item.getAmount() - 1);
            else
                player.setItemInHand(null);

        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getDamager();

        if (triggered.containsKey(player.getUniqueId())) {
            System.out.print("feast for " + player.getName());
            event.setDamage(event.getDamage() * 1.20);
        }

    }

}
