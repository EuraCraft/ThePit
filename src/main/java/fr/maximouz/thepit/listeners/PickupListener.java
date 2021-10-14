package fr.maximouz.thepit.listeners;

import fr.maximouz.thepit.utils.ItemStackUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupListener implements Listener {

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        Material type = event.getItem().getItemStack().getType();

        // Ne pas ramasser une pièce d'armure en fer/diamant si déjà 2 sont dans l'inventaire
        if ((ItemStackUtils.isIronArmorPiece(type) || ItemStackUtils.isDiamondArmorPiece(type)) && (ItemStackUtils.count(player.getInventory().getContents(), type) + ItemStackUtils.count(player.getInventory().getArmorContents(), type)) >= 2)
            event.setCancelled(true);
        else if (ItemStackUtils.equip(player, event.getItem().getItemStack(), true)) {
            event.setCancelled(true);
            event.getItem().remove();
        }

    }

}
