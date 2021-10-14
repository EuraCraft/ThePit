package fr.maximouz.thepit.upgrade.perk;

import org.bukkit.entity.Player;

public interface IPerk {

    void load(Player player);
    void save(Player player);

    void onSelected(Player player);
    void onUnselected(Player player);

}
