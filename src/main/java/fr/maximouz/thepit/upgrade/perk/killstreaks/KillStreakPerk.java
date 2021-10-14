package fr.maximouz.thepit.upgrade.perk.killstreaks;

import fr.maximouz.thepit.bank.Level;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.*;

public abstract class KillStreakPerk implements IKillStreakPerk, Listener {

    private final KillStreakPerkType type;

    private final List<UUID> playersBought;
    private final Map<UUID, KillStreakPerkSlot> playersSelected;

    public KillStreakPerk(KillStreakPerkType type) {
        this.type = type;
        this.playersBought = new ArrayList<>();
        this.playersSelected = new HashMap<>();
    }

    public KillStreakPerkType getType() {
        return type;
    }

    public String getDisplayName() {
        return type.getDisplayName();
    }

    public Material getMaterial() {
        return type.getMaterial();
    }

    public byte getData() {
        return type.getData();
    }

    public Level getLevelRequired() {
        return type.getLevelRequired();
    }

    public BigDecimal getPrice() {
        return type.getPrice();
    }

    public String[] getDescription() {
        return type.getDescription();
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

    public Map<UUID, KillStreakPerkSlot> getPlayersSelected() {
        return playersSelected;
    }

    public boolean hasSelected(Player player) {
        return getPlayersSelected().containsKey(player.getUniqueId());
    }

    public KillStreakPerkSlot getSelectedSlot(Player player) {
        return getPlayersSelected().get(player.getUniqueId());
    }

    public void select(Player player, KillStreakPerkSlot slot) {
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
    public void save(Player player) {
        removeBought(player);
        getPlayersSelected().remove(player.getUniqueId());
    }

    @Override
    public void onSelected(Player player) {}

    @Override
    public void onUnselected(Player player) {}

}
