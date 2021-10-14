package fr.maximouz.thepit.inventories.upgrade.perk;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.inventories.upgrade.UpgradesInventory;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.PerkSlot;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PerkChooseInventory extends AbstractInterface {

    public PerkChooseInventory(Player owner, Bank bank, PerkSlot perkSlot) {
        super(owner, "§8Choix de la compétence #" + perkSlot.getIntegerValue(), 4*9);

        List<PerkType> perkTypes = new ArrayList<>(Arrays.asList(PerkType.values()));

        List<Perk> selectedPerks = PerkManager.getInstance().getSelectedPerks(owner);

        int index = 10;

        while(!perkTypes.isEmpty()) {

            if (index == 17)
                index = 19;
            else if (index == 26)
                break;

            PerkType perkType = perkTypes.get(0);
            Perk perk = PerkManager.getInstance().getPerk(perkType);
            this.setItem(index++, initPerkItem(perk, owner, bank, selectedPerks.contains(perk)), event -> buy(perk, owner, bank, perkSlot));
            perkTypes.remove(perkType);

        }

        ItemStack backItem = new ItemBuilder(Material.ARROW)
                .setName("§aRetour")
                .setLore("§7Clic gauche pour retourner", "§7en arrière.")
                .build();

        this.setItem(31, backItem, event -> {
            new UpgradesInventory(owner, bank).open();
            owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

        if (PerkManager.getInstance().getPlayerPerk(owner, perkSlot) != null) {

            ItemStack noneItem = new ItemBuilder(Material.DIAMOND_BLOCK)
                    .setName("§cAucune compétence")
                    .setLore("§7Retire la compétence actuelle", "§7pour cet emplacement.", "", "§eClic gauche pour retirer.")
                    .build();

            this.setItem(32, noneItem, event -> {
                removePerk(owner, perkSlot);
                owner.playSound(owner.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
                new UpgradesInventory(owner, bank).open();
            });
        }

    }

    private void buy(Perk perk, Player player, Bank bank, PerkSlot perkSlot) {

        if (perk.getLevelRequired().level <= bank.getLevel().level) {

            if (perk.hasBought(player)) {

                if (!perk.hasSelected(player)) {

                    removePerk(player, perkSlot);
                    perk.select(player, perkSlot);
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 6f, 6f);
                    new UpgradesInventory(player, bank).open();

                } else {

                    player.sendMessage("§cVous avez déjà sélectionné cette compétence !");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);

                }

            } else {

                if (bank.getBalance().compareTo(perk.getPrice()) >= 0) {

                    new PerkPurchaseConfirmInventory(player, bank, perk, perkSlot).open();
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);

                } else {

                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    player.sendMessage("§cVous n'avez pas assez de Gold !");

                }

            }

        }

    }

    private void removePerk(Player player, PerkSlot perkSlot) {

        PerkManager.getInstance().unselectPlayerPerk(player, perkSlot);

    }

    private ItemStack initPerkItem(Perk perk, Player player, Bank bank, boolean selected) {

        ItemStack item = perk.getItemStack(player);
        ItemMeta meta = item.getItemMeta();

        if (perk.getLevelRequired().level <= bank.getLevel().level) {

            List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
            lore.add("");

            if (perk.hasBought(player)) {

                if (selected) {

                    meta.setDisplayName(ChatColor.GREEN + perk.getDisplayName());
                    meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore.add(ChatColor.GREEN + "Compétence déjà sélectionnée !");

                } else {

                    meta.setDisplayName(ChatColor.YELLOW + perk.getDisplayName());
                    lore.add(ChatColor.YELLOW + "Clic gauche pour sélectionner !");

                }

            } else {

                boolean canBuy = bank.getBalance().compareTo(perk.getPrice()) >= 0;

                meta.setDisplayName((canBuy ? ChatColor.GREEN : ChatColor.RED) + perk.getDisplayName());
                lore.add(ChatColor.GRAY + "Prix: " + ChatColor.GOLD + Format.format(perk.getPrice()) + "g");
                lore.add(canBuy ? ChatColor.YELLOW + "Clic gauche pour acheter !" : ChatColor.RED + "Vous n'avez pas assez de Gold !");

            }

            meta.setLore(lore);

        } else {

            meta.setDisplayName(ChatColor.RED + "Compétence inconnue");
            meta.setLore(Collections.singletonList(ChatColor.GRAY + "Niveau requis: " + bank.getPrestige().getColor() + "[" + perk.getLevelRequired().getLevel() + bank.getPrestige().getColor() + "]"));
            item.setType(Material.BEDROCK);

        }

        item.setItemMeta(meta);
        return item;

    }

}
