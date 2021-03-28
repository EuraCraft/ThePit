package fr.maximouz.thepit.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInitEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private ItemStack sword;
    private ItemStack helmet;
    private ItemStack chestPlate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack[] others;

    public PlayerInitEvent(Player player, ItemStack sword, ItemStack helmet, ItemStack chestPlate, ItemStack leggings, ItemStack boots, ItemStack... others) {
        super(player);
        setSword(sword);
        setHelmet(helmet);
        setChestPlate(chestPlate);
        setLeggings(leggings);
        setBoots(boots);
        setOthers(others);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getSword() {
        return sword;
    }

    public void setSword(ItemStack sword) {
        this.sword = sword;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getChestPlate() {
        return chestPlate;
    }

    public void setChestPlate(ItemStack chestPlate) {
        this.chestPlate = chestPlate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack[] getOthers() {
        return others;
    }

    public void setOthers(ItemStack[] others) {
        this.others = others;
    }

}
