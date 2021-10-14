package fr.maximouz.thepit.quest.quests;

import fr.maximouz.thepit.events.EarnExperienceEvent;
import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.math.BigDecimal;

public class ExperienceCollectQuest extends Quest {

    public ExperienceCollectQuest(QuestType type) {
        super(type);
    }

    @EventHandler
    public void onEarnExperience(EarnExperienceEvent event) {

        if (event.getAmount().compareTo(BigDecimal.ZERO) > 0 && !isInCoolDown(event.getBank().getOwner()))
            setProgression(event.getBank().getOwner(), getProgression(event.getBank().getOwner()) + event.getAmount().longValue());

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (event.getMessage().equalsIgnoreCase("finish collectionneur xp"))
            setProgression(event.getPlayer(), getAmount());

    }

}
