package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerInitEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

public class LavaBucketPerk extends Perk {

    public LavaBucketPerk() {
        super(PerkType.LAVA_BUCKET, "Seau de lave", Material.LAVA_BUCKET, Level.TEN, 1000.0, ChatColor.GRAY + "Vous apparaissez avec un", ChatColor.GRAY + "seau de lave.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @EventHandler (priority = EventPriority.LOW)
    public void onInit(PlayerInitEvent event) {

        Player player = event.getPlayer();
        if (hasSelected(player)) {

            ItemStack[] others = event.getOthers();
            for (int i = 0; i < others.length; i++) {
                if (others[i] == null || others[i].getType() == Material.AIR) {
                    others[i] = new ItemStack(getMaterial());
                    break;
                }
            }
            event.setOthers(others);

        }

    }
}
