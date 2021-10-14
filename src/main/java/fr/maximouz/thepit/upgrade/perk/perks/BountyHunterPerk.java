package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerAssistEvent;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.prime.Prime;
import fr.maximouz.thepit.prime.PrimeManager;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BountyHunterPerk extends Perk {

    public BountyHunterPerk() {
        super(PerkType.BOUNTY_HUNTER, "Chasseur de primes", Material.GOLD_LEGGINGS, Level.FORTY, 2000.0,
                "§7Vous gagnez §6+4g§7 lors d'un meurtre et un bonus",
                "§7sur les assistances qui avaient une prime.",
                "§c+1% §7de dégâts tous les §6100g§7 de prime."
        );
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.isCancelled() || event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (hasSelected(player)) {

            Prime prime = PrimeManager.getInstance().getPrime(target);

            if (prime != null) {



                double multiplier = prime.getGold().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP).add(BigDecimal.valueOf(1)).doubleValue();
                System.out.print("bounty hunter : multiplier = " + multiplier);
                event.setDamage(event.getDamage() * multiplier);

            }

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && hasSelected(player)) {

            System.out.print("bounty hunter +4g for " + event.getPlayer().getName());
            event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(4)));

        }

    }

    @EventHandler
    public void onAssist(PlayerAssistEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            Player victim = event.getVictim();

            Prime prime = PrimeManager.getInstance().getPrime(victim);
            if (prime != null) {
                BigDecimal reward = event.getGoldReward().add(prime.getGold().multiply(BigDecimal.valueOf(0.95)));
                System.out.print("bounty hunter +" + reward.toString() + "g for " + event.getPlayer().getName());
                event.setGoldReward(reward);
            }

        }

    }

}
