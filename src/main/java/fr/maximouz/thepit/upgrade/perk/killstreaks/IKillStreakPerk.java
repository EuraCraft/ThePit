package fr.maximouz.thepit.upgrade.perk.killstreaks;

import fr.maximouz.thepit.upgrade.perk.IPerk;
import org.bukkit.entity.Player;

public interface IKillStreakPerk extends IPerk {

    void trigger(Player player);

}
