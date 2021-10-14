package fr.maximouz.thepit.upgrade.upgrades;

import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.statistic.PlayerStatistic;
import fr.maximouz.thepit.statistic.PlayerStatisticsManager;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import fr.maximouz.thepit.utils.IntegerToRoman;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.*;

public class GatoUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersXKills;

    public GatoUpgrade(UpgradeType type) {
        super(type);
        this.tiersXKills = new HashMap<>();

        this.tiersXKills.put(1, 1);
        setPrice(1, 1000.0);
        setLevelRequired(1, Level.ONE);

        this.tiersXKills.put(2, 2);
        setPrice(2, 2000.0);
        setLevelRequired(2, Level.ONE);

        this.tiersXKills.put(3, 3);
        setPrice(3, 3000.0);
        setLevelRequired(3, Level.ONE);

        this.tiersXKills.put(4, 4);
        setPrice(4, 4000.0);
        setLevelRequired(4, Level.ONE);

        this.tiersXKills.put(5, 5);
        setPrice(5, 5000.0);
        setLevelRequired(5, Level.ONE);

        /*this.tiersXKills.put(6, 6);
        setPrice(6, 7500.0);
        setLevelRequired(6, Level.SEVENTY);*/
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
        int tier = Math.min(playerTier + 1, getMaxTier());
        BigDecimal tierPrice = getPrice(tier);
        int tierXKills = tiersXKills.get(tier);
        boolean canBuy = bank.getBalance().compareTo(tierPrice) >= 0;

        ItemStack item = new ItemBuilder(Material.CAKE).build();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName((canBuy ? ChatColor.GREEN : ChatColor.RED) + getDisplayName());

        List<String> lore = new ArrayList<>();
        lore.add("§7Bonus: " + getBonus(player));
        lore.add("§7Palier: " + ChatColor.GREEN + IntegerToRoman.toRoman(tier));
        lore.add("");
        lore.add("§7Prochain palier:");

        List<String> eachTierDescription = new ArrayList<>();
        for (String line : getEachTierDescription())
            eachTierDescription.add(line
                    .replace("%KILLS%" , tierXKills > 1 ? "§7Les §d" + tierXKills + " premiers meurtres" : "§7Le §dpremier meurtre")
                    .replace("rapportent", tierXKills <= 1 ? "rapporte" : "rapportent")
            );

        lore.addAll(eachTierDescription);
        lore.add("");

        Level levelRequired = getLevelRequired(tier);

        if (playerTier == getMaxTier()) {

            meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aPalier max débloqué !");

        } else if (bank.getLevel().level < levelRequired.level) {

            lore.add("§7Niveau requis: " + bank.getPrestige().getColor() + "[" + levelRequired.getLevel() + bank.getPrestige().getColor() + "]");
            lore.add(ChatColor.RED + "Votre niveau est trop bas !");

        } else {

            lore.add("§7Prix: " + ChatColor.GOLD + tierPrice.toString() + "g");
            lore.add(canBuy ? "§7Clic gauche pour acheter !" : "§cVous n'avez pas assez de Gold !");

        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public short getDyeColor() {
        return 0;
    }

    public int getTierFirstKills(int tier) {
        return tiersXKills.get(tier);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player == null)
            return;

        int playerTier = getTier(player);

        if (playerTier > 0) {

            PlayerStatistic playerStatistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(player);
            long killStreak = playerStatistic.getKillStreak();

            if (killStreak <= getTierFirstKills(playerTier)) {

                event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(5)));
                event.setExperienceReward(event.getExperienceReward().add(BigDecimal.valueOf(5)));

            }

        }

    }
}
