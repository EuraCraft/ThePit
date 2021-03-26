package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerReceiveGAppleEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class GoldenHeadsPerk extends Perk {

    public GoldenHeadsPerk() {
        super(PerkType.GOLDEN_HEADS, "goldenheads", ChatColor.YELLOW + "Têtes en or", Material.SKULL_ITEM, Level.ONE, 1000, ChatColor.GRAY + "Les pommes dorées que vous gagnez", ChatColor.GRAY + "sont des " + ChatColor.YELLOW + "têtes en or " + ChatColor.GRAY + "qui se", ChatColor.GRAY + "consomment instantanément.");
    }

    @Override
    public void load(Player player) {
        buy(player);
    }

    @Override
    public void save(Player player) {
        removeSelect(player);
        removeBought(player);
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        meta.setOwner("jmql");

        meta.setDisplayName(getDisplayName());
        meta.setLore(Arrays.asList(getDescription()));

        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onReceiveGoldenApple(PlayerReceiveGAppleEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            event.setCancelled(true);

            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(getDisplayName());
            item.setItemMeta(meta);

            player.getInventory().addItem(item);

        }

    }

}
