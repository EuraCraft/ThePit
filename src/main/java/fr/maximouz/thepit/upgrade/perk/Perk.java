package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.bank.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class Perk implements IPerk, Listener {

    private final PerkType type;
    private final String displayName;
    private final Material material;
    private final Level levelRequired;
    private final double price;
    private final String[] description;

    private final List<Player> playersBought;
    private final Map<Player, PerkSlot> playersSelected;

    public Perk(PerkType type, String displayName, Material material, Level levelRequired, double price, String... description) {
        this.type = type;
        this.displayName = displayName;
        this.material = material;
        this.levelRequired = levelRequired;
        this.price = price;
        this.description = description;
        this.playersBought = new ArrayList<>();
        this.playersSelected = new HashMap<>();
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

    public double getPrice() {
        return price;
    }

    public String[] getDescription() {
        return description;
    }

    public List<Player> getPlayersBought() {
        return playersBought;
    }

    public void buy(Player player) {
        getPlayersBought().add(player);
    }

    public boolean hasBought(Player player) {
        return getPlayersBought().contains(player);
    }

    public void removeBought(Player player) {
        getPlayersBought().remove(player);
    }

    public Map<Player, PerkSlot> getPlayersSelected() {
        return playersSelected;
    }

    public boolean hasSelected(Player player) {
        return getPlayersSelected().containsKey(player);
    }

    public PerkSlot getSelectedSlot(Player player) {
        return getPlayersSelected().get(player);
    }

    public void select(Player player, PerkSlot slot) {
        getPlayersSelected().put(player, slot);
        onSelect(player);
    }

    public void removeSelect(Player player) {
        getPlayersSelected().remove(player);
        onUnselect(player);
    }

    public void loadAll() {
        Bukkit.getOnlinePlayers().forEach(this::load);
    }

    public void saveAll() {
        Bukkit.getOnlinePlayers().forEach(this::save);
    }

    public ItemStack getItemStack(Player player) {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());
        meta.setLore(Arrays.asList(description));

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

    @Override
    public void onSelect(Player player) {}

    @Override
    public void onUnselect(Player player) {}

}
