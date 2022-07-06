package fr.maximouz.thepit.quest;

import org.bukkit.entity.Player;

import java.math.BigDecimal;

public interface IQuest {

    QuestType getType();
    QuestTime getTime();
    String getName();
    String getDescription();
    long getAmount();
    BigDecimal getReward();

    void load(Player player);
    void save(Player player);
    void complete(Player player);
    void onReset();

}
