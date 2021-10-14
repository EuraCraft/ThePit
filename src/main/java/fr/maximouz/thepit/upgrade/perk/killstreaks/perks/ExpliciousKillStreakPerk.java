package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpliciousKillStreakPerk extends KillStreakPerk {

    private final List<Player> triggeredPlayers;

    public ExpliciousKillStreakPerk(KillStreakPerkType type) {
        super(type);
        triggeredPlayers = new ArrayList<>();
    }

    @Override
    public void trigger(Player player) {
        triggeredPlayers.add(player);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        if (triggeredPlayers.contains(event.getPlayer())) {

            System.out.print("explicious for " + event.getPlayer().getName());
            event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(12)));
            triggeredPlayers.remove(event.getPlayer());

        }

    }

}
