package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class GladiatorPerk extends Perk {

    public GladiatorPerk() {
        super(PerkType.GLADIATOR, "Gladiateur", Material.BONE, Level.FIFTY, 4000.0, "§7Vous recevez §9-3%§7 de dégâts tous les", "§73 joueurs dans un rayon de 12", "§7blocs autour de vous.", "", "§7Minimum: 3 joueurs", "§7Maximum: 10 joueurs");
    }

    public double getReducedDamage(Player player) {
        long entitiesCount = player.getNearbyEntities(12, 12, 12).stream().filter(entity -> entity.getType() == EntityType.PLAYER).count();
        return entitiesCount >= 3 && entitiesCount <= 9 ? 3 * Math.floor((double) entitiesCount / 3) : 0;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getEntity();

        if (hasSelected(player)) {

            double reducedDamage = getReducedDamage(player);
            System.out.print("Gladiator: reduced damage for " + player.getName() + " = " + reducedDamage);

            if (reducedDamage > 0) {

                event.setDamage(event.getDamage() * reducedDamage / 100);

            }

        }

    }

}
