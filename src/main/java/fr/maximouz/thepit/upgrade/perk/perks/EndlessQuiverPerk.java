package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EndlessQuiverPerk extends Perk {

    public EndlessQuiverPerk() {
        super(PerkType.ENDLESS_QUIVER, "Carquois infini", Material.ARROW, Level.TEN, 2000.0, ChatColor.GRAY + "Vous gagnez " + ChatColor.WHITE + "3 flêches " + ChatColor.GRAY + "quand vous", ChatColor.GRAY + "tirez à l'arc sur un joueur.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.ARROW || !(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();

        if (hasSelected(player))
            player.getInventory().addItem(new ItemStack(Material.ARROW, 3));

    }

}
