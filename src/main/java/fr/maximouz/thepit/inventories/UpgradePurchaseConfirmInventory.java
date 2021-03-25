package fr.maximouz.thepit.inventories;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.utils.IntegerToRoman;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UpgradePurchaseConfirmInventory extends AbstractInterface {

    public UpgradePurchaseConfirmInventory(Player owner, Bank bank, ThePit main, Upgrade upgrade, int currentPlayerTier, double price) {
        super(owner, ChatColor.DARK_GRAY + "Confirmation de l'achat", 3 * 9);

        ItemStack acceptItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 5).setName(ChatColor.GREEN + "Continuer mon achat").setLore(ChatColor.GRAY + "Clic gauche pour " + ChatColor.GREEN + "accepter" + ChatColor.GRAY +" l'achat.").build();
        ItemStack cancelItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 14).setName(ChatColor.RED + "Annuler mon achat").setLore(ChatColor.GRAY + "Clic gauche pour " + ChatColor.RED + "annuler" + ChatColor.GRAY + " l'achat.").build();

        this.setItem(11, acceptItem, o -> {

            if (upgrade.getLevelRequired(currentPlayerTier + 1).level <= bank.getLevel().level) {

                if (bank.getBalance() >= price) {

                    bank.withdraw(price);
                    upgrade.setTier(owner, currentPlayerTier + 1);
                    owner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "ACHAT! " + ChatColor.GRAY + "Amélioration " + upgrade.getDisplayName() + " " + IntegerToRoman.toRoman(currentPlayerTier + 1));
                    owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    new UpgradesInventory(owner, bank, main).open();

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

        this.setItem(15, cancelItem, o -> new UpgradesInventory(owner, bank, main).open());

    }

}
