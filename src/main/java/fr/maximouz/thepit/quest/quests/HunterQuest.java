package fr.maximouz.thepit.quest.quests;

import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HunterQuest extends Quest {

    public HunterQuest(QuestType type) {
        super(type);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && !isInCoolDown(player)) {
            increaseProgression(player);
        }

    }

}
