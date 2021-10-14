package fr.maximouz.thepit.inventories.upgrade.perk.killstreak;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkSlot;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MegaStreakPurchaseConfirmInventory extends AbstractInterface {

    public MegaStreakPurchaseConfirmInventory(Player owner, Bank bank, KillStreakPerk perk) {
        super(owner, "§8Confirmation de l'achat", 3 * 9);

        ItemStack acceptItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 5).setName("§aContinuer mon achat").setLore("§7Clic gauche pour §aaccepter §7l'achat.").build();
        ItemStack cancelItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 14).setName("§cAnnuler mon achat").setLore("§cClic gauche pour §cannuler§7 l'achat.").build();

        this.setItem(11, acceptItem, event -> {

            if (bank.getLevel().level >= perk.getLevelRequired().level) {

                if (!perk.hasBought(owner)) {

                    if (bank.getBalance().compareTo(perk.getPrice()) >= 0) {

                        bank.withdraw(perk.getPrice());
                        perk.buy(owner);

                        PerkManager.getInstance().unselectPlayerKillStreakPerk(owner, KillStreakPerkSlot.THREE);
                        PerkManager.getInstance().selectPlayerKillStreakPerk(owner, perk, KillStreakPerkSlot.THREE);

                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                        owner.sendMessage("§a§lACHAT! §7Compétence " + perk.getDisplayName());

                    } else {

                        owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                        owner.sendMessage("§cVous n'avez pas assez de Gold.");

                    }

                }

            }

        });

        this.setItem(13, cancelItem, event -> {
            new MegaStreakChooseInventory(owner, bank).open();
            owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

    }

}
