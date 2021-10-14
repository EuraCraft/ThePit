package fr.maximouz.thepit.upgrade.perk.killstreaks;

public enum KillStreakPerkEvery {

    THREE(3),
    FIVE(5),
    SEVEN(7),
    TWENTY_FIVE(25),

    FIFTY(50);

    private final int integerValue;

    KillStreakPerkEvery(int integerValue) {
        this.integerValue = integerValue;
    }

    public int getIntegerValue() {
        return integerValue;
    }
}
