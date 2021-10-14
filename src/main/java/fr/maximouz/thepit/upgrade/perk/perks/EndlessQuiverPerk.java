package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerShootPlayerEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class EndlessQuiverPerk extends Perk {

    public EndlessQuiverPerk() {
        super(PerkType.ENDLESS_QUIVER, "Carquois infini", Material.ARROW, Level.TEN, 2000.0,
                "§7Vous gagnez §f3 flêches§7 quand vous",
                "§7tirez à l'arc sur un joueur.");
    }

    @EventHandler
    public void onShoot(PlayerShootPlayerEvent event) {

        if (event.isShootingHimself())
            return;

        Player player = event.getPlayer();

        if (hasSelected(player))
            player.getInventory().addItem(new ItemStack(Material.ARROW, 4));

    }

}
