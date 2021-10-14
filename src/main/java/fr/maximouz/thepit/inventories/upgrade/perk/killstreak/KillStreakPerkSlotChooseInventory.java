package fr.maximouz.thepit.inventories.upgrade.perk.killstreak;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.inventories.upgrade.UpgradesInventory;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkSlot;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KillStreakPerkSlotChooseInventory extends AbstractInterface {

    private final Player owner;
    private final Bank bank;

    public KillStreakPerkSlotChooseInventory(Player owner, Bank bank) {
        super(owner, "§8Série de meurtres", 3 * 9);
        this.owner = owner;
        this.bank = bank;

        ItemStack backItem = new ItemBuilder(Material.ARROW)
                .setName("§aRetour")
                .setLore("§7Clic gauche pour retourner", "§7en arrière.")
                .build();

        this.setItem(22, backItem, event -> {
            new UpgradesInventory(owner, bank).open();
            owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

        setupSlot(KillStreakPerkSlot.ONE, 11);
        setupSlot(KillStreakPerkSlot.TWO, 13);

        KillStreakPerk perk = PerkManager.getInstance().getPlayerKillStreakPerk(owner, KillStreakPerkSlot.THREE);

        ItemStack item = new ItemBuilder(perk.getMaterial(), 3)
                .setName("§e" + perk.getDisplayName())
                .setLore(
                        "§7Séléctionnée: §a" + perk.getDisplayName(),
                        "",
                        "§7Activation à: §c" + perk.getType().getEvery().getIntegerValue() + " meurtres",
                        ""
                )
                .addLore(perk.getDescription())
                .addLore("", "§eClic gauche pour changer.")
                .build();

        this.setItem(15, item, event -> {
            new MegaStreakChooseInventory(owner, bank).open();
            owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

    }

    private void setupSlot(KillStreakPerkSlot ksSlot, int invSlot) {

        KillStreakPerk perk = PerkManager.getInstance().getPlayerKillStreakPerk(owner, ksSlot);

        ItemBuilder slotItem = new ItemBuilder(perk != null ? perk.getMaterial() : Material.GOLD_BLOCK, ksSlot.getIntegerValue());

        if (perk != null && perk.getData() > 0)
            slotItem.setColor(perk.getData());

        if (bank.getPrestige().getIntegerValue() < ksSlot.getPrestigeRequired().getIntegerValue()) {

            slotItem.setType(Material.BEDROCK);
            slotItem.setName("§cCompétence #" + ksSlot.getIntegerValue());
            String color = bank.getPrestige().getColor();
            slotItem.setLore("§cNécessite le Prestige " + ksSlot.getPrestigeRequired().name, "", "§7Atteignez le niveau " + color + "[" + Level.ONE_HUNDRED_AND_TWENTY.getLevel() + color + "]§7 et", "§7parlez au PNJ Prestige !");

        } else {

            slotItem.setName("§aCompétence #" + ksSlot.getIntegerValue());

            if (perk == null) {

                slotItem.setLore("§7Choisissez une compétence", "§7pour cet emplacement", "", "§eClic gauche pour choisir.");

            } else {

                slotItem.setLore(
                        "§7Séléctionnée: §a" + perk.getDisplayName(),
                        "",
                        "§7Tous les: §c" + perk.getType().getEvery().getIntegerValue() + " meurtres",
                        ""
                )
                        .addLore(perk.getDescription())
                        .addLore("", "§eClic gauche pour changer.");

            }

        }

        this.setItem(invSlot, slotItem.build(), event -> {

            if (bank.getPrestige().getIntegerValue() < ksSlot.getPrestigeRequired().getIntegerValue()) {

                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

            } else {

                new KillStreakPerkChooseInventory(owner, bank, ksSlot).open();
                owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);

            }

        });

    }

}
