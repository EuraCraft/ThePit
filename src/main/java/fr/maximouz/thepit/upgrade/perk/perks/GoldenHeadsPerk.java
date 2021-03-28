package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerReceiveGAppleEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import fr.maximouz.thepit.utils.Reflection;
import fr.maximouz.thepit.utils.SkinUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class GoldenHeadsPerk extends Perk {

    private static final Class<?> humanClass = Reflection.getNMSClass("EntityHuman");

    public GoldenHeadsPerk() {
        super(PerkType.GOLDEN_HEADS, "Têtes en or", Material.SKULL_ITEM, Level.ONE, 1000, ChatColor.GRAY + "Les pommes dorées que vous gagnez", ChatColor.GRAY + "sont des " + ChatColor.YELLOW + "têtes en or " + ChatColor.GRAY + "qui se", ChatColor.GRAY + "consomment instantanément.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @Override
    public ItemStack getItemStack(Player player) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        SkinUtils.applyGoldenHead(meta);
        meta.setDisplayName(getDisplayName());
        meta.setLore(Arrays.asList(getDescription()));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public void onSelect(Player player) {

        player.getInventory().remove(Material.GOLDEN_APPLE);

    }

    @Override
    public void onUnselect(Player player) {

        player.getInventory().remove(getMaterial());

    }

    @EventHandler
    public void onReceiveGoldenApple(PlayerReceiveGAppleEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            event.setCancelled(true);

            ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            SkinUtils.applyGoldenHead(meta);
            meta.setDisplayName(ChatColor.YELLOW + "Tête en or");

            item.setItemMeta(meta);

            player.getInventory().addItem(item);

        }

    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.SKULL_ITEM && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Tête en or") && hasSelected(player)) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 4, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, 0));
            Object human = humanClass.cast(Reflection.getHandle(player));
            try {
                Reflection.callMethod(Reflection.getMethod(humanClass, "setAbsorptionHearts", float.class), human, 4.0f);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (item.getAmount() <= 1) {
                player.setItemInHand(null);
            } else {
                item.setAmount(item.getAmount() - 1);
            }

        }

    }

}
