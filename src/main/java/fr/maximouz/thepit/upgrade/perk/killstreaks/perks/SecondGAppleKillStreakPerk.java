package fr.maximouz.thepit.upgrade.perk.killstreaks.perks;

import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.events.PlayerReceiveGAppleEvent;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SecondGAppleKillStreakPerk extends KillStreakPerk {

    private final List<Player> triggeredPlayers;

    public SecondGAppleKillStreakPerk(KillStreakPerkType type) {
        super(type);
        triggeredPlayers = new ArrayList<>();
    }

    @Override
    public void trigger(Player player) {
        triggeredPlayers.add(player);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && hasSelected(player)) {

            System.out.print("Second gapple for " + player.getName() + " (gold, xp)");
            event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(5)));
            event.setExperienceReward(event.getExperienceReward().add(BigDecimal.valueOf(5)));

        }

    }

    @EventHandler (priority = EventPriority.LOW)
    public void onReceiveGApple(PlayerReceiveGAppleEvent event) {

        Player player = event.getPlayer();

        if (triggeredPlayers.contains(player)) {

            System.out.print("Second gapple for " + player.getName() + " (gapple)");
            ItemStack item = event.getItem();
            item.setAmount(item.getAmount() + 1);
            event.setItem(item);
            triggeredPlayers.remove(player);

        }

    }

}
