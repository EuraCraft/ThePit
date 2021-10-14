package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArquebusierKillStreakPerk extends KillStreakPerk {

    private final List<UUID> triggeredPlayers;

    public ArquebusierKillStreakPerk(KillStreakPerkType type) {
        super(type);
        triggeredPlayers = new ArrayList<>();
    }

    @Override
    public void trigger(Player player) {
        triggeredPlayers.add(player.getUniqueId());
        player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0));
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && triggeredPlayers.contains(event.getPlayer().getUniqueId())) {

            System.out.print("arquebusier for " + player.getName());
            event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(7)));
            triggeredPlayers.remove(event.getPlayer().getUniqueId());

        }

    }

}
