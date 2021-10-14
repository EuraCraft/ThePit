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

import java.math.BigDecimal;
import java.util.*;

public abstract class Upgrade implements IUpgrade, Listener {

    private final UpgradeType type;

    private final Map<UUID, Integer> tiers;
    private final Map<Integer, BigDecimal> tiersPrice;
    private final Map<Integer, Level> tiersLevelRequired;

    public Upgrade(UpgradeType type) {
        this.type = type;
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

    public String getDisplayName() {
        return type.getDisplayName();
    }

    public String[] getEachTierDescription() {
        return type.getDescription();
    }

    public int getTier(Player player) {
        return tiers.get(player.getUniqueId());
    }

    public void setTier(Player player, int tier) {
        tiers.put(player.getUniqueId(), tier);
    }

    public void removeTier(Player player) {
        tiers.remove(player.getUniqueId());
    }

    public BigDecimal getPrice(int tier) {
        return tiersPrice.get(tier);
    }

    public void setPrice(int tier, BigDecimal price) {
        tiersPrice.put(tier, price);
    }

    public void setPrice(int tier, double price) {
        tiersPrice.put(tier, BigDecimal.valueOf(price));
    }

    public Level getLevelRequired(int tier) {
        return tiersLevelRequired.get(tier);
    }

    public void setLevelRequired(int tier, Level level) {
        tiersLevelRequired.put(tier, level);
    }

    public ItemStack getItemStack(Player player, Bank bank) {

        int playerTier = getTier(player);
        int tier = Math.min(playerTier + 1, getMaxTier());
        BigDecimal tierPrice = getPrice(tier);
        boolean canBuy = bank.getBalance().compareTo(tierPrice) >= 0;

        ItemStack item = new ItemBuilder(Material.INK_SACK).setColor(getDyeColor()).build();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName((canBuy ? ChatColor.GREEN : ChatColor.RED) + getDisplayName());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);

        List<String> lore = new ArrayList<>();
        lore.add("§7Bonus: " + getBonus(player));
        lore.add("§7Palier: §a" + IntegerToRoman.toRoman(tier));
        lore.add("");
        lore.add("§7Chaque palier:");
        lore.addAll(Arrays.asList(getEachTierDescription()));
        lore.add("");

        if (playerTier == getMaxTier()) {

            meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lore.add("§aPalier max débloqué !");

        } else {

            Level levelRequired = getLevelRequired(tier);

            if (levelRequired.level > bank.getLevel().level) {

                lore.add("§7Niveau requis: " + bank.getPrestige().getColor() + "[" + levelRequired.getLevel() + bank.getPrestige().getColor() + "]");
                lore.add("§cVotre niveau est trop bas !");

            } else {

                lore.add("§7Prix: §6"+ tierPrice.toString() + "g");
                lore.add(canBuy ? "§eClic gauche pour acheter !" : "§cVous n'avez pas assez de Gold !");

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
