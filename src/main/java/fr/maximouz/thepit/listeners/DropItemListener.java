package fr.maximouz.thepit.listeners;

import fr.maximouz.thepit.events.PlayerKillEvent;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class DropItemListener implements Listener {

    private final List<Material> doNotDropItems;
    private final List<Material> removeOnDropItems;

    public DropItemListener() {

        doNotDropItems = Arrays.asList(
                Material.IRON_SWORD,
                Material.BOW,
                Material.ARROW,
                Material.GOLDEN_APPLE
        );

        removeOnDropItems = Arrays.asList(
                Material.CHAINMAIL_HELMET,
                Material.CHAINMAIL_CHESTPLATE,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_BOOTS
        );

    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();
        
        if (doNotDropItems.contains(itemStack.getType()))
            event.setCancelled(true);
        else if (removeOnDropItems.contains(itemStack.getType()))
            item.remove();
        
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        event.getDrops()
                .removeIf(item -> item != null && (doNotDropItems.contains(item.getType()) || removeOnDropItems.contains(item.getType())));

    }
    
}
