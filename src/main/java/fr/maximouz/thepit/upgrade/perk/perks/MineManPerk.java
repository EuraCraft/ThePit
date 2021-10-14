package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerInitEvent;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.items.PerkItem;
import fr.maximouz.thepit.items.TemporaryBlockItem;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class MineManPerk extends Perk {

    public MineManPerk() {
        super(PerkType.MINE_MAN, "Mineur", Material.COBBLESTONE, Level.TWENTY, 2000.0,
                "§7Vous apparaissez avec §f24 pierres§7 et",
                "§7une pioche en §bdiamant§7.",
                "",
                "§f+3 blocs§7 lorsque vous réalisez",
                "§7un meurtre."
        );
    }

    @Override
    public void onSelected(Player player) {
        player.getInventory().addItem(new TemporaryBlockItem(Material.STONE, 24, 30).build());
        player.getInventory().addItem(new PerkItem(Material.DIAMOND_PICKAXE, 1).build());
    }

    @Override
    public void onUnselected(Player player) {
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
                    others[i] = foundFreeSlot++ == 0 ? new PerkItem(Material.DIAMOND_PICKAXE, 1).build() : new TemporaryBlockItem(getMaterial(), 24, 30).build();
                }
            }
            event.setOthers(others);

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        event.removeMaterialFromDrop(getMaterial());

        Player player = event.getPlayer();

        if (player != null && hasSelected(player))
            player.getInventory().addItem(new ItemStack(Material.STONE, 3));

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        if (event.getItemDrop().getItemStack().getType() == Material.DIAMOND_PICKAXE || event.getItemDrop().getItemStack().getType() == Material.STONE)
            event.setCancelled(true);

    }

}
