package fr.maximouz.thepit.items;

import fr.euracraft.api.item.ItemBuilder;
import fr.euracraft.api.item.ItemEvent;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.events.ObsidianPlaceEvent;
import fr.maximouz.thepit.tasks.ObsidianBreakTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;

public class TemporaryObsidianItem extends ItemBuilder {

    public TemporaryObsidianItem(int amount) {

        super(Material.OBSIDIAN, amount);

        this.setEvent(ItemEvent.BlockPlace, o -> {

            BlockPlaceEvent event = (BlockPlaceEvent) o;
            if (!event.isCancelled()) {

                ObsidianPlaceEvent obsidianPlaceEvent = new ObsidianPlaceEvent(event.getPlayer(), event.getBlock().getLocation(), 120);
                Bukkit.getPluginManager().callEvent(obsidianPlaceEvent);

                if (!obsidianPlaceEvent.isCancelled()) {

                    ObsidianBreakTask task = new ObsidianBreakTask(obsidianPlaceEvent.getLocation());
                    task.runTaskLater(ThePit.getInstance(), 20 * obsidianPlaceEvent.getLifeTime());

                }

            }

        });
    }

}
