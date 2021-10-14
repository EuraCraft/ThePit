package fr.maximouz.thepit.upgrade.perk.killstreaks;

import fr.maximouz.thepit.bank.Prestige;

public enum  KillStreakPerkSlot {

    ONE(1, Prestige.ZERO),
    TWO(2, Prestige.ONE),
    THREE(3, Prestige.TEN);

    private final int integerValue;
    private final Prestige prestigeRequired;

    KillStreakPerkSlot(int integerValue, Prestige prestigeRequired) {
        this.integerValue = integerValue;
        this.prestigeRequired = prestigeRequired;
    }

    public int getIntegerValue() {
        return integerValue;
    }

    public Prestige getPrestigeRequired() {
        return prestigeRequired;
    }
}
