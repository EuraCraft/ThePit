package fr.maximouz.thepit.quest.quests;

import fr.maximouz.thepit.events.EarnGoldEvent;
import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.math.BigDecimal;

public class GoldCollectQuest extends Quest {

    public GoldCollectQuest(QuestType type) {
        super(type);
    }

    @EventHandler
    public void onEarnGold(EarnGoldEvent event) {

        if (event.getAmount().compareTo(BigDecimal.ZERO) > 0 && !isInCoolDown(event.getBank().getOwner()))
            setProgression(event.getBank().getOwner(), getProgression(event.getBank().getOwner()) + event.getAmount().longValue());

    }

}
