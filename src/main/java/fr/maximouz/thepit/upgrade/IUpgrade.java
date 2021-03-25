package fr.maximouz.thepit.upgrade;

import org.bukkit.entity.Player;

public interface IUpgrade {

    void load(Player player);
    void save(Player player);
    String getBonus(Player player);
    short getDyeColor();
    int getMaxTier();

}
