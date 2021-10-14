package fr.maximouz.thepit.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerKillEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Player victim;
    private BigDecimal experienceReward;
    private BigDecimal goldReward;
    private List<ItemStack> drops;

    public PlayerKillEvent(Player player, Player victim, BigDecimal experienceReward, BigDecimal goldReward, List<ItemStack> drops) {
        super(player);
        this.victim = victim;
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
        this.drops = drops;
    }

    public Player getVictim() {
        return victim;
    }

    public BigDecimal getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(BigDecimal experienceReward) {
        this.experienceReward = experienceReward;
    }

    public BigDecimal getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(BigDecimal goldReward) {
        this.goldReward = goldReward;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public void removeMaterialFromDrop(Material material) {
        drops = drops.stream().filter(item -> item != null && item.getType() != material).collect(Collectors.toList());
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
