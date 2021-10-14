package fr.maximouz.thepit.items;

import fr.euracraft.api.item.ItemBuilder;
import fr.euracraft.api.item.ItemEvent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PerkItem extends ItemBuilder {

    public PerkItem(Material type, int amount, int data) {
        super(type, amount, data);

        this.setEvent(ItemEvent.PlayerDropItem, o -> ((PlayerDropItemEvent)o).setCancelled(true));
    }

    public PerkItem(Material type, int amount) {
        this(type, amount, 0);
    }

}
