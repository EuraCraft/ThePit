package fr.maximouz.thepit.quest.quests;

import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ContractQuest extends Quest {

    public ContractQuest(QuestType type) {
        super(type);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        if (event.getMessage().equalsIgnoreCase("finish contrat"))
            increaseProgression(event.getPlayer());

    }

}
