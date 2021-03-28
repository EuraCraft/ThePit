package fr.maximouz.thepit.inventories;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeManager;
import fr.maximouz.thepit.upgrade.UpgradeType;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.PerkSlot;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import fr.euracraft.api.gui.AbstractInterface;
import fr.maximouz.thepit.utils.SkinUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpgradesInventory extends AbstractInterface {

    private final Player owner;
    private final ThePit main;

    public UpgradesInventory(Player owner, Bank bank, ThePit main) {
        super(owner, ChatColor.DARK_GRAY + "Améliorations permanantes", 5*9);
        this.owner = owner;
        this.main = main;

        PerkManager perkManager = PerkManager.getInstance();

        int inventorySlot = 12;
        for (PerkSlot perkSlot : PerkSlot.values()) {

            Perk perk = perkManager.getPlayerPerk(owner, perkSlot);

            if (perk == null) {

                perk = perkManager.getPerk(PerkType.NONE);

            }

            ItemStack item = new ItemStack(Material.BEDROCK);
            ItemMeta meta = item.getItemMeta();

            if (bank.getLevel().level >= perkSlot.getLevelRequired().level) {

                List<String> lore = new ArrayList<>();

                if (perk.getType() == PerkType.NONE) {

                    item.setType(Material.DIAMOND_BLOCK);
                    meta.setDisplayName(ChatColor.GREEN + "Compétence #" + perkSlot.getIntegerValue());
                    lore.add(ChatColor.GRAY + "Choisissez une compétence pour");
                    lore.add(ChatColor.GRAY + "remplir cet emplacement.");

                } else {

                    if (perk.getMaterial() == Material.SKULL_ITEM) {

                        item = new ItemStack(perk.getMaterial(), 1, (byte) 3);
                        meta = item.getItemMeta();
                        SkinUtils.applyGoldenHead((SkullMeta) meta);

                    } else {

                        item.setType(perk.getMaterial());

                    }

                    meta.setDisplayName(ChatColor.YELLOW + "Compétence #" + perkSlot.getIntegerValue());
                    lore.add(ChatColor.GRAY + "Sélectionnée: " + perk.getDisplayName());
                    lore.add("");
                    lore.addAll(Arrays.asList(perk.getDescription()));
                }

                meta.setLore(lore);

            } else {

                meta.setDisplayName(ChatColor.RED + "Compétence #" + perkSlot.getIntegerValue());
                meta.setLore(Collections.singletonList(ChatColor.GRAY + "Niveau requis: " + bank.getPrestige().getColor() + "[" + perkSlot.getLevelRequired().getLevel() + bank.getPrestige().getColor() + "]"));

            }

            item.setItemMeta(meta);

            this.setItem(inventorySlot++, item, event -> {

                if (bank.getLevel().level >= perkSlot.getLevelRequired().level) {

                    new PerkChooseInventory(owner, bank, main, perkSlot).open();

                } else {

                    owner.sendMessage(ChatColor.RED + "Votre niveau est trop bas !");
                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

                }
            });

        }

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

    }

    private void buy(Upgrade upgrade, Player player, Bank bank) {
        int currentPlayerTier = upgrade.getTier(player);

        if (upgrade.getMaxTier() > currentPlayerTier) {

            if (upgrade.getLevelRequired(currentPlayerTier + 1).level <= bank.getLevel().level) {

                double price = upgrade.getPrice(currentPlayerTier + 1);

                if (bank.getBalance() >= price) {

                    new UpgradePurchaseConfirmInventory(player, bank, main, upgrade, currentPlayerTier, price).open();

                } else {

                    player.sendMessage(ChatColor.RED + "Vous n'avez pas assez de Gold !");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

                }

            } else {

                player.sendMessage(ChatColor.RED + "Votre niveau est trop bas !");
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);

            }

        }
    }

    @Override
    public void open() {
        super.open();
        owner.playSound(owner.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f);
    }
}
