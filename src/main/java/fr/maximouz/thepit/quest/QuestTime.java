package fr.maximouz.thepit.quest;

import java.util.concurrent.TimeUnit;

public enum QuestTime {

    DAILY(TimeUnit.DAYS.toMillis(1), "journalière"),
    WEEKLY(TimeUnit.DAYS.toMillis(7), "hebdomadaire");

    private final long coolDown;
    private final String stringValue;

    QuestTime(long coolDown, String stringValue) {
        this.coolDown = coolDown;
        this.stringValue = stringValue;
    }

    public long getCoolDown() {
        return coolDown;
    }

    public String getStringValue() {
        return stringValue;
    }
}
