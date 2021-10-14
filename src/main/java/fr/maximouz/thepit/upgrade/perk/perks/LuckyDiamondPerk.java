package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.items.PerkItem;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import fr.maximouz.thepit.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class LuckyDiamondPerk extends Perk {


    public LuckyDiamondPerk() {
        super(PerkType.LUCKY_DIAMOND, "Diamant chanceux", Material.DIAMOND, Level.THIRTY, 4000.0,
                "§7Vous avez 30% de chances de récuperer une",
                "§7pièce d'armure en §bdiamant§7 à la",
                "§7place d'une pièce en fer."
        );
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();

        if (!ItemStackUtils.isIronArmorPiece(itemStack.getType()))
            return;

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            if (Math.random() <= 0.3) {

                System.out.print("lucky diamond for " + player.getName() + " (" + itemStack.getType().toString() + ")");

                ItemStack newItem = new PerkItem(ItemStackUtils.isChestPlate(itemStack)
                            ? Material.DIAMOND_CHESTPLATE
                            : ItemStackUtils.isLeggings(itemStack)
                                ? Material.DIAMOND_LEGGINGS
                                : ItemStackUtils.isBoots(itemStack)
                                    ? Material.DIAMOND_BOOTS
                                    : Material.AIR, 1)
                        .setUnbreakable(true)
                        .build();

                if (ItemStackUtils.equip(player, newItem, true)) {

                    event.setCancelled(true);
                    item.remove();
                    
                }

            }

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && hasSelected(player) && (player.getInventory().getHelmet() == null || !ItemStackUtils.isDiamondArmorPiece(player.getInventory().getHelmet()))) {

            Player victim = event.getVictim();

            if (victim.getInventory().getHelmet() != null && (ItemStackUtils.isChainMailArmorPiece(victim.getInventory().getHelmet()) || ItemStackUtils.isIronArmorPiece(victim.getInventory().getHelmet()))) {

                if (Math.random() <= 0.3) {
                    System.out.print("lucky diamond helmet for " + player.getName());
                    player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                }

            }
        }

    }

}
