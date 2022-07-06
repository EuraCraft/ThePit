package fr.maximouz.thepit.quest.quests;

import fr.maximouz.thepit.events.QuestCompleteEvent;
import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.math.BigDecimal;

public class ContractQuest extends Quest {

    public ContractQuest(QuestType type) {
        super(type);
    }

    @EventHandler
    public void onQuestComplete(QuestCompleteEvent event) {

        if (!isInCoolDown(event.getPlayer()))
            increaseProgression(event.getPlayer());

    }

}
