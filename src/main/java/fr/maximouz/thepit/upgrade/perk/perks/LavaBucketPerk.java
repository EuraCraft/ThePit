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
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class LavaBucketPerk extends Perk {

    public LavaBucketPerk() {
        super(PerkType.LAVA_BUCKET, "Seau de lave", Material.LAVA_BUCKET, Level.TEN, 1000.0,
                "§7Vous apparaissez avec",
                "§7un seau de lave.");
    }

    @Override
    public void onSelected(Player player) {
        player.getInventory().addItem(new ItemStack(getMaterial()));
    }

    @Override
    public void onUnselected(Player player) {
        player.getInventory().remove(getMaterial());
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onInit(PlayerInitEvent event) {

        Player player = event.getPlayer();
        if (hasSelected(player)) {

            ItemStack[] others = event.getOthers();
            for (int i = 0; i < others.length; i++) {
                if (others[i] == null || others[i].getType() == Material.AIR) {
                    ItemStack item = new PerkItem(getMaterial(), 1)
                            .build();
                    others[i] = item;
                    break;
                }
            }
            event.setOthers(others);

        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        if (event.getItemDrop().getItemStack().getType() == Material.LAVA_BUCKET)
            event.setCancelled(true);

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        event.removeMaterialFromDrop(getMaterial());

    }

}
