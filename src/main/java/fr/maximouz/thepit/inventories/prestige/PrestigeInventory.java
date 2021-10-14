package fr.maximouz.thepit.inventories.prestige;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.bank.Prestige;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PrestigeInventory extends AbstractInterface {

    public PrestigeInventory(Player owner) {
        super(owner, "§8Prestige & Renommée", 3*9);

        Bank bank = BankManager.getInstance().getBank(owner);

        ItemBuilder prestigeItem = new ItemBuilder(Material.DIAMOND)
                .setName("§bPrestige");

        if (bank.getPrestige() != Prestige.THIRTY_FIVE) {

            Prestige nextPrestige = bank.getPrestige().getNextPrestige();

            prestigeItem.setLore(
                    "§7Niveau requis: " + bank.getPrestige().getColor() + "[" + Level.ONE_HUNDRED_AND_TWENTY.getLevel() + bank.getPrestige().getColor() + "]",
                    "",
                    "§7Coûts:",
                    "§c§l ■ Reset §r§7du §bniveau§7 à §c1",
                    "§c§l ■ Reset §r§7du §6gold§7 à §c0",
                    "§c§l ■ Reset §r§7(toutes) §aaméliorations et §acompétences",
                    "§c§l ■ Reset §r§cde l'inventaire",
                    "§c§l ■ Payer §r§6" + nextPrestige.getPrice().toString() + "g",
                    "§7§oAméliorations de renommée conservées.",
                    "§7§oEnderChest conservé.",
                    "",
                    "§7Récompense: §b" + nextPrestige.getRenownReward().toString() + " renommées",
                    "",
                    "§7Prestige suivant: §b" + nextPrestige.getName(),
                    "§b+" + nextPrestige.getXpNeededMultiplier() +"%§7 d'xp requis qu'avant."

            );

        } else {

            prestigeItem.setLore("§7§mNiveau requis:§r§c vous êtes au Prestige max");

        }

    }

}
