package fr.maximouz.thepit.events;

import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class QuestCompleteEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private final Quest quest;

    public QuestCompleteEvent(Player player, Quest quest) {
        super(player);
        this.quest = quest;
    }

    public Quest getQuest() {
        return quest;
    }

    public QuestType getType() {
        return getQuest().getType();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
