package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.bank.Level;

public enum PerkSlot {

    ONE(1, Level.ONE),
    TWO(2, Level.THIRTY_FIVE),
    THREE(3, Level.SEVENTY_FIVE);

    private final int integerValue;
    private final Level levelRequired;

    PerkSlot(int integerValue, Level levelRequired) {
        this.integerValue = integerValue;
        this.levelRequired = levelRequired;
    }

    public int getIntegerValue() {
        return integerValue;
    }

    public Level getLevelRequired() {
        return levelRequired;
    }
}
