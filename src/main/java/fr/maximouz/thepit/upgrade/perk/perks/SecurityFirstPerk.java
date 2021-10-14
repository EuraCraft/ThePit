package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerInitEvent;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.items.PerkItem;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class SecurityFirstPerk extends Perk {

    public SecurityFirstPerk() {
        super(PerkType.SECURITY_FIRST, "Aucun risque", Material.CHAINMAIL_HELMET, Level.TWENTY, 3000,
                "§7Vous apparaissez avec un",
                "§7casque en maille."
        );
    }

    @Override
    public void onSelected(Player player) {
        player.getInventory().setHelmet(new ItemStack(getMaterial()));
    }

    @Override
    public void onUnselected(Player player) {
        player.getInventory().setHelmet(null);
    }

    @EventHandler
    public void onInit(PlayerInitEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            ItemStack item = new PerkItem(getMaterial(), 1)
                    .build();

            event.setHelmet(item);

        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        if (event.getItemDrop().getItemStack().getType() == getMaterial())
            event.setCancelled(true);

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        event.removeMaterialFromDrop(getMaterial());

    }

}
