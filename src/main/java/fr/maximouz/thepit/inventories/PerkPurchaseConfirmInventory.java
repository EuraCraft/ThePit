package fr.maximouz.thepit.inventories;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.PerkSlot;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PerkPurchaseConfirmInventory extends AbstractInterface {

    public PerkPurchaseConfirmInventory(Player owner, Bank bank, ThePit main, Perk perk, PerkSlot perkSlot) {
        super(owner, ChatColor.DARK_GRAY + "Confirmation de l'achat", 3 * 9);

        ItemStack acceptItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 5).setName(ChatColor.GREEN + "Continuer mon achat").setLore(ChatColor.GRAY + "Clic gauche pour " + ChatColor.GREEN + "accepter" + ChatColor.GRAY +" l'achat.").build();
        ItemStack cancelItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 14).setName(ChatColor.RED + "Annuler mon achat").setLore(ChatColor.GRAY + "Clic gauche pour " + ChatColor.RED + "annuler" + ChatColor.GRAY + " l'achat.").build();

        this.setItem(11, acceptItem, o -> {

            if (perk.getLevelRequired().level <= bank.getLevel().level) {

                if (bank.getBalance() >= perk.getPrice()) {

                    bank.withdraw(perk.getPrice());
                    perk.buy(owner);
                    PerkManager.getInstance().removePlayerPerk(owner, perkSlot);
                    perk.select(owner, perkSlot);

                    owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    owner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "ACHAT! " + ChatColor.GRAY + "Compétence " + perk.getDisplayName());

                    new PerkChooseInventory(owner, bank, main, perkSlot).open();

                } else {

                    owner.sendMessage(ChatColor.RED + "Vous n'avez pas assez de Gold !");
                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    owner.closeInventory();

                }

            } else {

                owner.sendMessage(ChatColor.RED + "Votre niveau est trop bas !");
                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                owner.closeInventory();

            }

        });

        this.setItem(15, cancelItem, o -> new PerkChooseInventory(owner, bank, main, perkSlot).open());

    }

}
