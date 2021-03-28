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

public class MineManPerk extends Perk {

    public MineManPerk() {
        super(PerkType.MINE_MAN, "Mineur", Material.COBBLESTONE, Level.TWENTY, 2000.0, ChatColor.GRAY + "Vous apparaissez avec " + ChatColor.WHITE + "24 pierres " + ChatColor.GRAY + "et", ChatColor.GRAY + "une pioche en " + ChatColor.AQUA + "diamant" + ChatColor.GRAY + ".", "", ChatColor.WHITE + "+3 blocs " + ChatColor.GRAY + "lorsque vous réalisez", ChatColor.GRAY + "un meurtre.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @Override
    public void onUnselect(Player player) {
        player.getInventory().remove(getMaterial());
        player.getInventory().remove(Material.DIAMOND_PICKAXE);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onInit(PlayerInitEvent event) {

        Player player = event.getPlayer();
        if (hasSelected(player)) {

            int foundFreeSlot = 0;

            ItemStack[] others = event.getOthers();
            for (int i = 0; i < others.length; i++) {
                if (foundFreeSlot == 2)
                    break;
                if (others[i] == null || others[i].getType() == Material.AIR) {
                    others[i] = foundFreeSlot++ == 0 ? new ItemStack(Material.DIAMOND_PICKAXE) : new ItemStack(getMaterial(), 24);
                }
            }
            event.setOthers(others);

        }

    }

}
