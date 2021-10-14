package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.prime.PrimeManager;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;

public class StreakerPerk extends Perk {

    public StreakerPerk() {
        super(PerkType.STREAKER, "Maître des séries", Material.WHEAT, Level.FORTY, 8000.0,
                "§7Votre bonus d'XP sur les §bséries§7 de",
                "§7meurtres est triplé."
        );
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && PrimeManager.getInstance().hasBounty(player)) {
            System.out.print("streaker for " + player.getName());
            event.setExperienceReward(event.getExperienceReward().multiply(BigDecimal.valueOf(3)));
        }

    }

}
