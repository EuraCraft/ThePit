package fr.maximouz.thepit.quest;

import fr.maximouz.thepit.quest.quests.*;
import fr.maximouz.thepit.utils.Reflection;

import java.math.BigDecimal;

public enum QuestType {

    HUNTER(HunterQuest.class, QuestTime.DAILY, "Chasseur", "Tuer 25 joueurs", 25, 1000.0),
    CONTRACT(ContractQuest.class, QuestTime.DAILY, "Contrat", "Compléter un contrat", 1, 1000),
    VISIT(VisitQuest.class, QuestTime.DAILY, "Visiteur", "Visiter toutes les zones", 5, 250),

    GOLD_COLLECT(GoldCollectQuest.class, QuestTime.WEEKLY, "Collectionneur", "Collecter 10,000g", 10000, 5000.0),
    EXPERIENCE_COLLECT(ExperienceCollectQuest.class, QuestTime.WEEKLY, "Collectionneur", "Collecter 10,000XP", 10000, 5000.0);

    private final Class<?> clazz;
    private final QuestTime questTime;
    private final String name;
    private final String description;
    private final long amount;
    private final BigDecimal reward;

    QuestType(Class<?> clazz, QuestTime questTime, String name, String description, long amount, double reward) {
        this.clazz = clazz;
        this.questTime = questTime;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.reward = BigDecimal.valueOf(reward);
    }

    public QuestTime getQuestTime() {
        return questTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public Quest getNewInstance() {
        try {
            return (Quest) Reflection.callConstructor(Reflection.getConstructor(clazz, getClass()), this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
