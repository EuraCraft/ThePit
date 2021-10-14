package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkSlot;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OverDriveKillStreakPerk extends KillStreakPerk {

    public OverDriveKillStreakPerk(KillStreakPerkType type) {
        super(type);
    }

    @Override
    public void load(Player player) {
        buy(player);
        select(player, KillStreakPerkSlot.THREE);
    }

    @Override
    public void save(Player player) {
        unselect(player);
    }

    @Override
    public void trigger(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 255, 0));
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

    }

}
