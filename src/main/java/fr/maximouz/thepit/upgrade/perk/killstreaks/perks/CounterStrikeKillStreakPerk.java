package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.euracraft.api.util.SymbolUtil;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CounterStrikeKillStreakPerk extends KillStreakPerk {

    private final List<UUID> triggered;

    public CounterStrikeKillStreakPerk(KillStreakPerkType type) {
        super(type);
        triggered = new ArrayList<>();
    }

    @Override
    public void trigger(Player player) {
        triggered.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> triggered.remove(player.getUniqueId()), 20 * 8);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() == EntityType.PLAYER) {

            Player player = (Player) event.getDamager();

            if (triggered.contains(player.getUniqueId())) {
                System.out.print("counter strike for " + player.getName() + " (+15%)");
                event.setDamage(event.getDamage() * 1.15);
            }

        }

        if (event.getEntity().getType() == EntityType.PLAYER) {

            Player player = (Player) event.getEntity();
            if (triggered.contains(player.getUniqueId())) {
                System.out.print("counter strike for " + player.getName() + " (blocking 1" + SymbolUtil.HEARTH + ")");
                event.setDamage(Math.max(0, event.getDamage() - 2));
            }

        }

    }

}
