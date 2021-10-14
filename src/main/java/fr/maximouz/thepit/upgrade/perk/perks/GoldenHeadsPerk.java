package fr.maximouz.thepit.upgrade.perk.perks;

import fr.euracraft.api.util.SymbolUtil;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.events.PlayerReceiveGAppleEvent;
import fr.maximouz.thepit.items.PerkItem;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.PerkSlot;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import fr.maximouz.thepit.utils.Reflection;
import fr.maximouz.thepit.utils.SkinUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class GoldenHeadsPerk extends Perk {

    private static final Class<?> humanClass = Reflection.getNMSClass("EntityHuman");

    public GoldenHeadsPerk() {
        super(PerkType.GOLDEN_HEADS, "Têtes en or", Material.SKULL_ITEM, Level.ONE, 1000,
                "§7Les pommes dorées que vous gagnez",
                "§7sont des §etêtes en or§7.",
                "",
                "§7§oConsommation instantanée."
        );
    }

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
    public void onSelected(Player player) {

        player.getInventory().remove(Material.GOLDEN_APPLE);

        for (PerkSlot perkSlot : PerkSlot.values()) {

            Perk perk = PerkManager.getInstance().getPlayerPerk(player, perkSlot);
            if (perk != null && perk.getType() == PerkType.VAMPIRE) {

                PerkManager.getInstance().unselectPlayerPerk(player, perkSlot);
                player.sendMessage("§c§lATTENTION! §7La compétence §cVampire§7 a été retirée à cause de son incompatibilité avec la compétence §eTêtes en or§7.");
                return;

            }

        }

    }

    @Override
    public void onUnselected(Player player) {

        player.getInventory().remove(getMaterial());

    }

    @EventHandler
    public void onReceiveGoldenApple(PlayerReceiveGAppleEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            ItemStack item = new PerkItem(Material.SKULL_ITEM, 1, (byte) 3)
                    .setName("§eTête en or")
                    .setLore("§dRégénération II (3s)", "§fRapidité I (7s)", "§7+3 §6" + SymbolUtil.HEARTH + " d'absorption")
                    .build();
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            SkinUtils.applyGoldenHead(meta);

            item.setItemMeta(meta);

            event.setItem(item);

        }

    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.SKULL_ITEM && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Tête en or") && hasSelected(player)) {

            player.playSound(player.getLocation(), Sound.EAT, 1f, 1f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 4, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, 0));
            Object human = humanClass.cast(Reflection.getHandle(player));
            try {
                float absorption = Reflection.callMethod(Reflection.getMethod(humanClass, "getAbsorptionHearts"), player);
                if (absorption < 6f) {
                    Reflection.callMethod(Reflection.getMethod(humanClass, "setAbsorptionHearts", float.class), human, 6.0f);
                }
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

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        if (event.getItemDrop().getItemStack().getType() == Material.SKULL_ITEM)
            event.setCancelled(true);

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        event.removeMaterialFromDrop(getMaterial());

    }

}
