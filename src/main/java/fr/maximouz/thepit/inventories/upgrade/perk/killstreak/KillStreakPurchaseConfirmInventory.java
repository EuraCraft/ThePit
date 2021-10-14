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

public class KillStreakPurchaseConfirmInventory extends AbstractInterface {

    public KillStreakPurchaseConfirmInventory(Player owner, Bank bank, KillStreakPerk perk, KillStreakPerkSlot perkSlot) {
        super(owner, "§8Confirmation de l'achat", 3 * 9);

        ItemStack acceptItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 5).setName("§aContinuer mon achat").setLore("§7Clic gauche pour §aaccepter §7l'achat.").build();
        ItemStack cancelItem = new ItemBuilder(Material.STAINED_CLAY).setColor((short) 14).setName("§cAnnuler mon achat").setLore("§cClic gauche pour §cannuler§7 l'achat.").build();

        this.setItem(11, acceptItem, o -> {

            if (perk.getLevelRequired().level <= bank.getLevel().level) {

                if (bank.getBalance().compareTo(perk.getPrice()) >= 0) {

                    bank.withdraw(perk.getPrice());
                    perk.buy(owner);
                    PerkManager.getInstance().unselectPlayerKillStreakPerk(owner, perkSlot);
                    PerkManager.getInstance().selectPlayerKillStreakPerk(owner, perk, perkSlot);

                    owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    owner.sendMessage("§a§lACHAT! §7Compétence " + perk.getDisplayName());

                    new KillStreakPerkSlotChooseInventory(owner, bank).open();

                } else {

                    owner.sendMessage("§cVous n'avez pas assez de Gold !");
                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    owner.closeInventory();

                }

            } else {

                owner.sendMessage("§cVotre niveau est trop bas !");
                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                owner.closeInventory();

            }

        });

        this.setItem(15, cancelItem, o -> {
            new KillStreakPerkChooseInventory(owner, bank, perkSlot).open();
            owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

    }

}
