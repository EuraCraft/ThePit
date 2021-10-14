package fr.maximouz.thepit.inventories.upgrade;

import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.inventories.upgrade.perk.PerkChooseInventory;
import fr.maximouz.thepit.inventories.upgrade.perk.killstreak.KillStreakPerkSlotChooseInventory;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeManager;
import fr.maximouz.thepit.upgrade.UpgradeType;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.PerkSlot;
import fr.euracraft.api.gui.AbstractInterface;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkEvery;
import fr.maximouz.thepit.utils.SkinUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpgradesInventory extends AbstractInterface {

    private final Player owner;

    public UpgradesInventory(Player owner, Bank bank) {
        super(owner, "§8Améliorations permanantes", 5*9);
        this.owner = owner;

        PerkManager perkManager = PerkManager.getInstance();

        int inventorySlot = 12;
        for (PerkSlot perkSlot : PerkSlot.values()) {

            Perk perk = perkManager.getPlayerPerk(owner, perkSlot);

            ItemStack item = new ItemStack(Material.BEDROCK);
            item.setAmount(perkSlot.getIntegerValue());
            ItemMeta meta = item.getItemMeta();

            if (bank.getLevel().level >= perkSlot.getLevelRequired().level) {

                List<String> lore = new ArrayList<>();

                if (perk == null) {

                    item.setType(Material.DIAMOND_BLOCK);
                    meta.setDisplayName("§aCompétence #" + perkSlot.getIntegerValue());
                    lore.add("§7Choisissez une compétence pour");
                    lore.add("§7cet emplacement.");

                } else {

                    if (perk.getMaterial() == Material.SKULL_ITEM) {

                        item = new ItemStack(perk.getMaterial(), 1, (byte) 3);
                        meta = item.getItemMeta();
                        SkinUtils.applyGoldenHead((SkullMeta) meta);

                    } else {

                        item.setType(perk.getMaterial());

                    }

                    meta.setDisplayName("§eCompétence #" + perkSlot.getIntegerValue());
                    lore.add("§7Sélectionnée: " + perk.getDisplayName());
                    lore.add("");
                    lore.addAll(Arrays.asList(perk.getDescription()));
                }

                lore.add("");
                lore.add("§eClic gauche pour choisir.");
                meta.setLore(lore);

            } else {

                meta.setDisplayName("§cCompétence #" + perkSlot.getIntegerValue());
                meta.setLore(Collections.singletonList("§7Niveau requis: " + bank.getPrestige().getColor() + "[" + perkSlot.getLevelRequired().getLevel() + bank.getPrestige().getColor() + "]"));

            }

            item.setItemMeta(meta);

            this.setItem(inventorySlot++, item, event -> {

                if (bank.getLevel().level >= perkSlot.getLevelRequired().level) {

                    new PerkChooseInventory(owner, bank, perkSlot).open();
                    owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);

                } else {

                    owner.sendMessage(ChatColor.RED + "Votre niveau est trop bas !");
                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

                }
            });

        }

        // Améliorations
        UpgradeManager upgradeManager = UpgradeManager.getInstance();

        Upgrade xpBoostUpgrade = upgradeManager.getUpgrade(UpgradeType.XP_BOOST);
        this.setItem(28, xpBoostUpgrade.getItemStack(owner, bank), event -> buy(xpBoostUpgrade, owner, bank));

        Upgrade goldBoostUpgrade = upgradeManager.getUpgrade(UpgradeType.GOLD_BOOST);
        this.setItem(29, goldBoostUpgrade.getItemStack(owner, bank), event -> buy(goldBoostUpgrade, owner, bank));

        Upgrade assassinUpgrade = upgradeManager.getUpgrade(UpgradeType.ASSASSIN);
        this.setItem(30, assassinUpgrade.getItemStack(owner, bank), event -> buy(assassinUpgrade, owner, bank));

        Upgrade sniperUpgrade = upgradeManager.getUpgrade(UpgradeType.SNIPER);
        this.setItem(31, sniperUpgrade.getItemStack(owner, bank), event -> buy(sniperUpgrade, owner, bank));

        Upgrade tankUpgrade = upgradeManager.getUpgrade(UpgradeType.TANK);
        this.setItem(32, tankUpgrade.getItemStack(owner, bank), event -> buy(tankUpgrade, owner, bank));

        Upgrade builderUpgrade = upgradeManager.getUpgrade(UpgradeType.BUILDER);
        this.setItem(33, builderUpgrade.getItemStack(owner, bank), event -> buy(builderUpgrade, owner, bank));

        Upgrade gatoUpgrade = upgradeManager.getUpgrade(UpgradeType.GATO);
        this.setItem(34, gatoUpgrade.getItemStack(owner, bank), event -> buy(gatoUpgrade, owner, bank));

        // item Compétence de série de meurtres
        int levelRequired = 10;
        ItemBuilder ksItem = new ItemBuilder(bank.getLevel().level >= levelRequired ? Material.BLAZE_POWDER : Material.BEDROCK)
                .setName(bank.getLevel().level >= levelRequired ? "§aSérie de meurtres" : "§cCompétence inconnue");

        if (bank.getLevel().level >= levelRequired) {
            List<KillStreakPerk> killStreakPerks = PerkManager.getInstance().getSelectedKillStreakPerk(owner);
            ksItem.setLore(
                    "§7Choissisez une compétence qui va",
                    "§7s'activer à chaque fois que vous",
                    "§7réalisez §cX§7 meurtres.",
                    ""
            );
            killStreakPerks.forEach(perk -> {
                if (perk.getType().getEvery() == KillStreakPerkEvery.FIFTY)
                    ksItem.addLore("§7Megastreak: §a" + perk.getDisplayName());
                else
                    ksItem.addLore("§7Tous les §c" + perk.getType().getEvery().getIntegerValue() + "§7 meurtres: §a" + perk.getDisplayName());
            });
            if (killStreakPerks.size() > 0)
                ksItem.addLore("", "§eClic gauche pour choisir.");
            else
                ksItem.addLore("§eClic gauche pour choisir.");
        }
        else
            ksItem.setLore("§7Niveau requis: " + bank.getPrestige().getColor() + "[§7" + levelRequired + bank.getPrestige().getColor() + "]");

        this.setItem(16, ksItem.build(), event -> {

            if (bank.getLevel().level >= 10) {

                new KillStreakPerkSlotChooseInventory(owner, bank).open();
                owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f);

            } else {

                owner.sendMessage("§cVotre niveau est trop bas !");
                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

            }

        });
    }

    private void buy(Upgrade upgrade, Player player, Bank bank) {
        int currentPlayerTier = upgrade.getTier(player);

        if (upgrade.getMaxTier() > currentPlayerTier) {

            if (upgrade.getLevelRequired(currentPlayerTier + 1).level <= bank.getLevel().level) {

                BigDecimal price = upgrade.getPrice(currentPlayerTier + 1);

                if (bank.getBalance().compareTo(price) >= 0) {

                    new UpgradePurchaseConfirmInventory(player, bank, upgrade, currentPlayerTier, price).open();
                    owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 1f, 1f);

                } else {

                    player.sendMessage("§cVous n'avez pas assez de Gold !");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

                }

            } else {

                player.sendMessage("§cVotre niveau est trop bas !");
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

            }

        }
    }

}
