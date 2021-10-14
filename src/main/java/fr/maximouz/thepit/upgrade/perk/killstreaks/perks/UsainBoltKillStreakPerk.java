package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UsainBoltKillStreakPerk extends KillStreakPerk {

    public UsainBoltKillStreakPerk(KillStreakPerkType type) {
        super(type);
    }

    @Override
    public void trigger(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 4, 1));
    }
}
