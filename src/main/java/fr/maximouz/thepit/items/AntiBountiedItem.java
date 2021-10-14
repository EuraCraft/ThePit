package fr.maximouz.thepit.items;

import fr.euracraft.api.item.ItemBuilder;
import fr.euracraft.api.item.ItemEvent;
import fr.maximouz.thepit.prime.PrimeManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AntiBountiedItem extends ItemBuilder {

    private static final long serialVersionUID = 1L;

    public AntiBountiedItem() {
        super(Material.DIAMOND_SWORD, 1);
        this.setUnbreakable(true);

        this.setEvent(ItemEvent.EntityDamageByEntity, o -> {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) o;

            if (event.getEntity().getType() != EntityType.PLAYER)
                return;

            Player damaged = (Player) event.getEntity();

            if (PrimeManager.getInstance().hasBounty(damaged)) {

                event.setDamage(event.getDamage() * 1.20);

            }

        });
    }

}
