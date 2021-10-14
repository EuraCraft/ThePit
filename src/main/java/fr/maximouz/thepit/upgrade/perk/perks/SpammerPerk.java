package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerAssistEvent;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.events.PlayerShootPlayerEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpammerPerk extends Perk {

    private final List<UUID> players;

    public SpammerPerk() {
        super(PerkType.SPAMMER, "Spammer", Material.STRING, Level.FORTY, 4000.0,
                "§7Vous recevez §c2x§7 plus de §6Gold§7 lorsque vous",
                "§7tuez un joueur à l'arc et vous gagnez",
                "§7§6+2g§7 sur les assistances.");
        players = new ArrayList<>();
    }

    @EventHandler
    public void onShoot(PlayerShootPlayerEvent event) {

        if (event.isShootingHimself())
            return;

        Player player = event.getPlayer();

        if (hasSelected(player) && player.getHealth() - event.getFinalDamage() <= 0.0D) {

            System.out.print("spammer for " + player.getName() + " (kill)");
            players.add(player.getUniqueId());

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && players.contains(player.getUniqueId())) {
            event.setGoldReward(event.getGoldReward().multiply(BigDecimal.valueOf(2)));
            players.remove(player.getUniqueId());
        }

    }

    @EventHandler
    public void onAssist(PlayerAssistEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {
            System.out.print("spammer for " + player.getName() + " (assist)");
            event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(2)));
        }

    }

}
