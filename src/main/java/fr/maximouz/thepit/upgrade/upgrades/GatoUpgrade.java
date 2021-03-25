package fr.maximouz.thepit.upgrade.upgrades;

import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import fr.maximouz.thepit.utils.IntegerToRoman;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GatoUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersXKills;

    public GatoUpgrade() {
        super(UpgradeType.GATO, "gato", ChatColor.GREEN + "Gourmet", "%KILLS% " + ChatColor.GRAY + "après votre mort", ChatColor.GRAY + "rapportent " + ChatColor.GOLD + "+5g" + ChatColor.GRAY + " et " + ChatColor.AQUA + "+5 XP" + ChatColor.GRAY + "." );
        this.tiersXKills = new HashMap<>();

        this.tiersXKills.put(1, 1);
        setPrice(1, 2000.0);
        setLevelRequired(1, Level.ONE);

        this.tiersXKills.put(2, 2);
        setPrice(2, 2000.0);
        setLevelRequired(2, Level.TWO);
    }

    @Override
    public void load(Player player) {
        setTier(player, 0);
    }

    @Override
    public void save(Player player) {
        removeTier(player);
    }

    @Override
    public String getBonus(Player player) {
        int playerTier = getTier(player);
        int tierXKills = tiersXKills.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return tierXKills > 1 ? ChatColor.LIGHT_PURPLE + "" + tierXKills + " premiers meurtres" : ChatColor.LIGHT_PURPLE + "Premier meurtre";
    }

    @Override
    public int getMaxTier() {
        return tiersXKills.size();
    }

    @Override
    public ItemStack getItemStack(Player player, Bank bank) {
        int playerTier = getTier(player);
        double tierPrice = getPrice(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        int tierXKills = tiersXKills.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);

        ItemStack item = new ItemBuilder(Material.CAKE).build();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Bonus: " + getBonus(player));
        lore.add(ChatColor.GRAY + "Palier: " + ChatColor.GREEN + IntegerToRoman.toRoman(playerTier == getMaxTier() ? playerTier : playerTier + 1));
        lore.add("");
        lore.add(ChatColor.GRAY + "Prochain palier:");

        List<String> eachTierDescription = new ArrayList<>();
        for (String line : getEachTierDescription())
            eachTierDescription.add(line
                    .replace("%KILLS%" , tierXKills > 1 ? ChatColor.GRAY + "Les " + ChatColor.LIGHT_PURPLE + "" + tierXKills + " premiers meurtres" : ChatColor.GRAY + "Le " + ChatColor.LIGHT_PURPLE + "premier meurtre")
                    .replace("rapportent", tierXKills <= 1 ? "rapporte" : "rapportent")
            );

        lore.addAll(eachTierDescription);
        lore.add("");

        if (playerTier == getMaxTier()) {

            lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Vous avez atteint le dernier palier !");
            meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        } else if (bank.getLevel().level < getLevelRequired(playerTier + 1).level) {

            lore.add(ChatColor.GRAY + "Niveau requis: " + bank.getPrestige().getColor() + "[" + Level.getLevel(bank.getLevel().level + 1).getLevel() + bank.getPrestige().getColor() + "]");
            lore.add(ChatColor.RED + "Votre niveau est trop bas !");

        } else {

            lore.add(ChatColor.GRAY + "Prix: " + ChatColor.GOLD + String.format("$%,.2f", tierPrice) + "g");
            lore.add(bank.getBalance() >= tierPrice ? ChatColor.YELLOW + "Clic gauche pour acheter !" : ChatColor.RED + "Vous n'avez pas assez de Gold !");

        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public short getDyeColor() {
        return 0;
    }
}
