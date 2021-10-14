package fr.maximouz.thepit.items;

import fr.euracraft.api.item.ItemBuilder;
import fr.euracraft.api.item.ItemEvent;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.events.TempBlockPlaceEvent;
import fr.maximouz.thepit.tasks.BlockBreakTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class TemporaryBlockItem extends ItemBuilder {

    public TemporaryBlockItem(Material material, int amount, int defaultLifeTime) {

        super(material, amount);

        this.setEvent(ItemEvent.BlockPlace, o -> {

            BlockPlaceEvent event = (BlockPlaceEvent) o;
            if (!event.isCancelled()) {

                TempBlockPlaceEvent tempBlockPlaceEvent = new TempBlockPlaceEvent(event.getPlayer(), event.getBlock().getLocation(), defaultLifeTime);
                Bukkit.getPluginManager().callEvent(tempBlockPlaceEvent);

                if (!tempBlockPlaceEvent.isCancelled()) {

                    BlockBreakTask task = new BlockBreakTask(tempBlockPlaceEvent.getLocation());
                    task.runTaskLater(ThePit.getInstance(), 20 * tempBlockPlaceEvent.getLifeTime());

                }

            }

        });

        this.setEvent(ItemEvent.BlockBreak, o -> {

            BlockBreakEvent event = (BlockBreakEvent) o;
            if (!event.isCancelled()) {

                BlockBreakTask task = BlockBreakTask.getTask(event.getBlock().getLocation());
                if (task != null) {
                    task.cancel();
                    BlockBreakTask.tasks.remove(task);
                    event.getBlock().setType(Material.AIR);
                    event.setCancelled(true);
                }

            }
        });

    }

}
