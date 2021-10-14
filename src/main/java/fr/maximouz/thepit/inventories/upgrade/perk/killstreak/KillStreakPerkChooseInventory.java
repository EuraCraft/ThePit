package fr.maximouz.thepit.inventories.upgrade.perk.killstreak;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkEvery;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkSlot;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KillStreakPerkChooseInventory extends AbstractInterface {

    public KillStreakPerkChooseInventory(Player owner, Bank bank, KillStreakPerkSlot ksSlot) {
        super(owner, "§8Choisissez une compétence", 6 * 9);
        setup(owner, bank, ksSlot);
    }

    private void setup(Player player, Bank bank, KillStreakPerkSlot ksSlot) {

        int slot = 10;

        for (KillStreakPerkEvery every : KillStreakPerkEvery.values()) {

            if (every.getIntegerValue() > KillStreakPerkEvery.TWENTY_FIVE.getIntegerValue())
                break;

            ItemBuilder item = new ItemBuilder(Material.ITEM_FRAME)
                    .setName("§c" + every.getIntegerValue() + " meurtres");

            List<KillStreakPerk> selectedKsPerks = PerkManager.getInstance().getSelectedKillStreakPerk(player);
            selectedKsPerks.stream()
                    .filter(perk -> perk.getType().getEvery() == every)
                    .findFirst()
                    .ifPresent(selectedPerkForThisEvery -> item.addEnchant(Enchantment.DIG_SPEED, 1)
                            .hideEnchants()
                            .setLore("§7Séléctionnée: §e" + selectedPerkForThisEvery.getDisplayName())
                    );

            this.setItem(slot++, item.build(), event -> player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f));

            List<KillStreakPerkType> types = KillStreakPerkType.get(every);

            for (int i = 0; i < Math.min(types.size(), 6); i++) {

                KillStreakPerk perk = PerkManager.getInstance().getKillStreakPerk(types.get(i));

                boolean hasLevelRequired = bank.getLevel().level >= perk.getLevelRequired().level;
                boolean hasBought = perk.hasBought(player);
                boolean hasSelected = selectedKsPerks.contains(perk);
                boolean canBuy = bank.getBalance().compareTo(perk.getPrice()) >= 0;

                ItemBuilder perkItem = new ItemBuilder(hasLevelRequired ? perk.getMaterial() : Material.BEDROCK)
                        .setName(hasLevelRequired ? hasBought ? "§a" + perk.getDisplayName() : (canBuy ? "§e" : "§c") + perk.getDisplayName() : "§cCompétence inconnue");

                if (perk.getData() > 0)
                    perkItem.setColor(perk.getData());

                if (hasLevelRequired) {

                    perkItem.setLore("§7Tous les: §c" + every.getIntegerValue() + " meurtres", "").addLore(perk.getDescription()).addLore("");

                    if (hasBought)
                        perkItem.addLore(hasSelected ? "§aDéjà séléctionnée !" : "§eClic gauche pour séléctionner !");
                    else
                        perkItem.addLore("§7Prix: §6" + Format.format(perk.getPrice()) + "g", canBuy ? "§eClic gauche pour acheter !" : "§cPas assez de Gold !");

                } else {

                    perkItem.setLore("§7Niveau requis: " + bank.getPrestige().getColor() + "[" + perk.getLevelRequired().level + bank.getPrestige().getColor() + "]");

                }

                this.setItem(slot + i, perkItem.build(), event -> buyOrSelect(player, bank, ksSlot, perk));

            }

            slot += 8;

        }

        ItemStack backItem = new ItemBuilder(Material.ARROW)
                .setName("§aRetour")
                .setLore("§7Clic gauche pour retourner", "§7en arrière.")
                .build();

        this.setItem(49, backItem, event -> {
            new KillStreakPerkSlotChooseInventory(player, bank).open();
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        });

        if (PerkManager.getInstance().getPlayerKillStreakPerk(player, ksSlot) != null) {

            ItemBuilder removePerkItem = new ItemBuilder(Material.GOLD_BLOCK)
                    .setName("§cAucune compétence")
                    .setLore("§7Retire la compétence actuelle", "§7pour cet emplacement.", "", "§eClic gauche pour retirer.");

            this.setItem(50, removePerkItem.build(), event -> {

                PerkManager.getInstance().unselectPlayerKillStreakPerk(player, ksSlot);
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
                new KillStreakPerkSlotChooseInventory(player, bank).open();

            });

        }

    }

    private void buyOrSelect(Player player, Bank bank, KillStreakPerkSlot ksSlot, KillStreakPerk perk) {

        List<KillStreakPerk> selectedKsPerks = PerkManager.getInstance().getSelectedKillStreakPerk(player);

        boolean hasLevelRequired = bank.getLevel().level >= perk.getLevelRequired().level;
        boolean hasBought = perk.hasBought(player);
        boolean canBuy = bank.getBalance().compareTo(perk.getPrice()) >= 0;

        if (hasLevelRequired) {

            if (hasBought) {

                if (!selectedKsPerks.contains(perk)) {

                    PerkManager.getInstance().unselectPlayerKillStreakPerk(player, ksSlot);
                    PerkManager.getInstance().selectPlayerKillStreakPerk(player, perk, ksSlot);
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 6f, 6f);
                    new KillStreakPerkSlotChooseInventory(player, bank).open();

                } else {

                    player.sendMessage("§cVous avez déjà sélectionné cette compétence !");
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);

                }

            } else {

                if (canBuy) {

                    new KillStreakPurchaseConfirmInventory(player, bank, perk, ksSlot).open();
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);

                } else {

                    player.sendMessage("§cVous n'avez pas assez de Gold !");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

                }

            }

        } else {

            player.sendMessage("§cVotre niveau est trop bas !");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

        }

    }

}
