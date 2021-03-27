package fr.maximouz.thepit.upgrade.perk;

import org.bukkit.entity.Player;

public interface IPerk {

    void load(Player player);
    void save(Player player);
    void onSelect(Player player);
    void onUnselect(Player player);

}
