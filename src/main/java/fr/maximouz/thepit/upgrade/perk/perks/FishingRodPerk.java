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

public class FishingRodPerk extends Perk {

    public FishingRodPerk() {
        super(PerkType.FISHING_ROD, "Pêcheur", Material.FISHING_ROD, Level.TEN, 1000.0, ChatColor.GRAY + "Vous apparaissez avec une", ChatColor.GRAY + "canne à pêche.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @Override
    public void onUnselect(Player player) {
        player.getInventory().remove(getMaterial());
    }

    @EventHandler (priority = EventPriority.NORMAL)
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
