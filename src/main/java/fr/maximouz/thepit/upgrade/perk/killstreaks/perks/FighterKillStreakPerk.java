package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FighterKillStreakPerk extends KillStreakPerk {

    private final List<UUID> triggeredPlayers;

    public FighterKillStreakPerk(KillStreakPerkType type) {
        super(type);
        triggeredPlayers = new ArrayList<>();
    }

    @Override
    public void trigger(Player player) {
        triggeredPlayers.add(player.getUniqueId());
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 4, 0));
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> triggeredPlayers.remove(player.getUniqueId()), 20 * 4);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getDamager();

        if (triggeredPlayers.contains(player.getUniqueId())) {
            System.out.print("fighter for " + player.getName());
            event.setDamage(event.getDamage() * 1.20);
        }

    }

}
