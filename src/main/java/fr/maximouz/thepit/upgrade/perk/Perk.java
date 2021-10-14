package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.bank.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.*;

public abstract class Perk implements IPerk, Listener {

    private final PerkType type;
    private final String displayName;
    private final Material material;
    private final Level levelRequired;
    private final BigDecimal price;
    private final String[] description;

    private final List<UUID> playersBought;
    private final Map<UUID, PerkSlot> playersSelected;

    public Perk(PerkType type, String displayName, Material material, Level levelRequired, BigDecimal price, String... description) {
        this.type = type;
        this.displayName = displayName;
        this.material = material;
        this.levelRequired = levelRequired;
        this.price = price;
        this.description = description;
        this.playersBought = new ArrayList<>();
        this.playersSelected = new HashMap<>();
    }

    public Perk(PerkType type, String displayName, Material material, Level levelRequired, double price, String... description) {
        this(type, displayName, material, levelRequired, BigDecimal.valueOf(price), description);
    }

    public PerkType getType() {
        return type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public Level getLevelRequired() {
        return levelRequired;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String[] getDescription() {
        return description;
    }

    public List<UUID> getPlayersBought() {
        return playersBought;
    }

    public void buy(Player player) {
        getPlayersBought().add(player.getUniqueId());
    }

    public boolean hasBought(Player player) {
        return getPlayersBought().contains(player.getUniqueId());
    }

    public void removeBought(Player player) {
        getPlayersBought().remove(player.getUniqueId());
    }

    public Map<UUID, PerkSlot> getPlayersSelected() {
        return playersSelected;
    }

    public boolean hasSelected(Player player) {
        return getPlayersSelected().containsKey(player.getUniqueId());
    }

    public PerkSlot getSelectedSlot(Player player) {
        return getPlayersSelected().get(player.getUniqueId());
    }

    public void select(Player player, PerkSlot slot) {
        getPlayersSelected().put(player.getUniqueId(), slot);
        onSelected(player);
    }

    public void unselect(Player player) {
        getPlayersSelected().remove(player.getUniqueId());
        onUnselected(player);
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    public void saveAll() {
        Bukkit.getOnlinePlayers().forEach(this::save);
    }

    public ItemStack getItemStack(Player player) {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(getDisplayName());
        meta.setLore(Arrays.asList(description));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public void onSelected(Player player) {}

    @Override
    public void onUnselected(Player player) {}

}
