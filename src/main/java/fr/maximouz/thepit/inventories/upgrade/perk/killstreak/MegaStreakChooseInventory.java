package fr.maximouz.thepit.inventories.upgrade.perk.killstreak;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkEvery;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkSlot;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MegaStreakChooseInventory extends AbstractInterface {

    private final Player owner;
    private final Bank bank;

    public MegaStreakChooseInventory(Player owner, Bank bank) {
        super(owner, "§8Megastreak", 3 * 9);
        this.owner = owner;
        this.bank = bank;

        int slot = 10;

        for (KillStreakPerkType type : KillStreakPerkType.get(KillStreakPerkEvery.FIFTY)) {

            if (slot == 17)
                return;

            KillStreakPerk perk = PerkManager.getInstance().getKillStreakPerk(type);

            boolean hasBought = perk.hasBought(owner);
            boolean hasSelected = perk.hasSelected(owner);
            boolean canBuy = bank.getBalance().compareTo(perk.getPrice()) >= 0;

            ItemBuilder item = new ItemBuilder(perk.getMaterial());

            if (perk.getLevelRequired().level > bank.getLevel().level) {

                item.setName("§cCompétence inconnue.");
                item.setLore("§7Niveau requis: " + bank.getPrestige().getColor() + "[" + perk.getLevelRequired().getLevel() + bank.getPrestige().getColor() + "]");

            } else {

                item.setName((hasBought ? hasSelected ? "§a" : "§e" : canBuy ? "§e" : "§c") + perk.getDisplayName())
                    .setLore("§7Activation à: §c" + type.getEvery().getIntegerValue() + " meurtres", "")
                    .addLore(perk.getDescription())
                    .addLore("", hasBought ? hasSelected ? "§aDéjà séléctionnée !" : "§eClic gauche pour séléctionner." : canBuy ? "§eClic gauche pour acheter." : "§cVous n'avez pas assez de Gold.");

            }

            setItem(slot, item.build(), event -> buyOrSelect(perk));

            slot++;

        }

        ItemStack backItem = new ItemBuilder(Material.ARROW)
                .setName("§aRetour")
                .setLore("§7Clic gauche pour retourner", "§7en arrière.")
                .build();

        this.setItem(22, backItem, event -> {
            new KillStreakPerkSlotChooseInventory(owner, bank).open();
            owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

    }

    private void buyOrSelect(KillStreakPerk perk) {

        if (bank.getLevel().level >= perk.getLevelRequired().level) {

            if (perk.hasBought(owner)) {

                if (perk.hasSelected(owner)) {

                    owner.sendMessage("§cVous avez déjà séléctionné cette compétence.");
                    owner.playSound(owner.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);

                } else {

                    PerkManager.getInstance().unselectPlayerKillStreakPerk(owner, KillStreakPerkSlot.THREE);
                    PerkManager.getInstance().selectPlayerKillStreakPerk(owner, perk, KillStreakPerkSlot.THREE);
                    owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 6f, 6f);
                    new KillStreakPerkSlotChooseInventory(owner, bank).open();

                }

            } else {

                if (bank.getBalance().compareTo(perk.getPrice()) >= 0) {

                    new MegaStreakPurchaseConfirmInventory(owner, bank, perk).open();

                } else {

                    owner.sendMessage("§cVous n'avez pas assez de Gold.");
                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

                }

            }

        }

    }

}
