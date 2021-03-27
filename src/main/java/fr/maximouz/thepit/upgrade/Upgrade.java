package fr.maximouz.thepit.upgrade;

import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.utils.Format;
import fr.maximouz.thepit.utils.IntegerToRoman;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class Upgrade implements IUpgrade, Listener {

    private final UpgradeType type;
    private final String name;
    private final String displayName;
    private final String[] eachTierDescription;

    private final Map<Player, Integer> tiers;
    private final Map<Integer, Double> tiersPrice;
    private final Map<Integer, Level> tiersLevelRequired;

    public Upgrade(UpgradeType type, String name, String displayName, String... eachTierDescription) {
        this.type = type;
        this.name = name;
        this.displayName = displayName;
        this.eachTierDescription = eachTierDescription;
        this.tiers = new HashMap<>();
        this.tiersPrice = new HashMap<>();
        this.tiersLevelRequired = new HashMap<>();
    }

    public void loadAll() {
        Bukkit.getOnlinePlayers().forEach(this::load);
    }

    public void saveAll() {
        Bukkit.getOnlinePlayers().forEach(this::save);
    }

    public UpgradeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String[] getEachTierDescription() {
        return eachTierDescription;
    }

    public int getTier(Player player) {
        return tiers.get(player);
    }

    public void setTier(Player player, int tier) {
        tiers.put(player, tier);
    }

    public void removeTier(Player player) {
        tiers.remove(player);
    }

    public double getPrice(int tier) {
        return tiersPrice.get(tier);
    }

    public void setPrice(int tier, double price) {
        tiersPrice.put(tier, price);
    }

    public Level getLevelRequired(int tier) {
        return tiersLevelRequired.get(tier);
    }

    public void setLevelRequired(int tier, Level level) {
        tiersLevelRequired.put(tier, level);
    }

    public ItemStack getItemStack(Player player, Bank bank) {

        int playerTier = getTier(player);
        double tierPrice = getPrice(playerTier == getMaxTier() ? playerTier : playerTier + 1);

        ItemStack item = new ItemBuilder(Material.INK_SACK).setColor(getDyeColor()).build();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Bonus: " + getBonus(player));
        lore.add(ChatColor.GRAY + "Palier: " + ChatColor.GREEN + IntegerToRoman.toRoman(playerTier == getMaxTier() ? playerTier : playerTier + 1));
        lore.add("");
        lore.add(ChatColor.GRAY + "Chaque palier:");
        lore.addAll(Arrays.asList(getEachTierDescription()));
        lore.add("");

        if (playerTier == getMaxTier()) {

            lore.add(ChatColor.GREEN + "" + ChatColor.BOLD + "Vous avez atteint le dernier palier !");
            meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        } else {

            Level levelRequired = getLevelRequired(playerTier + 1);

            if (levelRequired.level > bank.getLevel().level) {

                lore.add(ChatColor.GRAY + "Niveau requis: " + bank.getPrestige().getColor() + "[" + levelRequired.getLevel() + bank.getPrestige().getColor() + "]");
                lore.add(ChatColor.RED + "Votre niveau est trop bas !");

            } else {

                lore.add(ChatColor.GRAY + "Prix: " + ChatColor.GOLD + Format.format(tierPrice) + "g");
                lore.add(bank.getBalance() >= tierPrice ? ChatColor.YELLOW + "Clic gauche pour acheter !" : ChatColor.RED + "Vous n'avez pas assez de Gold !");

            }

        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        load(event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        save(event.getPlayer());

    }


}
