package fr.maximouz.thepit.bank;

import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Arrays;

public enum Prestige {

    // 0 - 9
    ZERO("", "&7", 0, 0, 0, 1),
    ONE("I", "&3", 1, 10000, 10, 1.1),
    TWO("II", "&3", 2, 22000, 10, 1.2),
    THREE("III", "&3", 3, 24000, 10, 1.3),
    FOUR("IV", "&3", 4, 26000, 10, 1.4),
    FIVE("V", "&e", 5, 28000, 20, 1.5),
    SIX("VI", "&e", 6, 30000, 20, 1.6),
    SEVEN("VII", "&e", 7, 70000, 20, 1.7),
    EIGHT("VIII", "&e", 8, 80000, 20, 1.8),
    NINE("IX", "&e", 9, 100000, 20, 1.9),

    // 10 - 19
    TEN("X", "&6", 10, 120000, 20, 2),
    ELEVEN("XI", "&6", 11, 120000, 30, 2.1),
    TWELVE("XII", "&6", 12, 200000, 30, 2.2),
    THIRTEEN("XIII", "&6", 13, 240000, 30, 2.3),
    FOURTEEN("XIV", "&6", 14, 280000, 30, 2.4),
    FIFTEEN("XV", "&c", 15, 320000, 40, 2.5),
    SIXTEEN("XVI", "&c",  16, 360000, 40, 2.6),
    SEVENTEEN("XVII", "&c", 17, 440000, 50, 2.7),
    EIGHTEEN("XVIII", "&c", 18, 480000, 50, 2.8),
    NINETEEN("XIX", "&c", 19, 500000, 60, 2.9),

    // 20 - 29
    TWENTY("XX", "&5", 20, 550000, 60, 3),
    TWENTY_ONE("XXI", "&5", 21, 600000, 70, 3.1),
    TWENTY_TWO("XXII", "&5", 22, 620000, 70, 3.2),
    TWENTY_THREE("XXIII", "&5", 23, 640000, 80, 3.3),
    TWENTY_FOUR("XXIV", "&5", 24, 660000, 80, 3.4),
    TWENTY_FIVE("XXV", "&d", 25, 680000, 90, 3.5),
    TWENTY_SIX("XXVI", "&d", 26, 700000, 90, 3.6),
    TWENTY_SEVEN("XXVII", "&d", 27, 740000, 100, 3.7),
    TWENTY_EIGHT("XXVIII", "&d", 28, 780000, 100, 3.8),
    TWENTY_NINE("XXIX", "&d", 29, 820000, 110, 3.9),

    // 30 - 35
    THIRTY("XXX", "&b", 30, 860000, 110, 4),
    THIRTY_ONE("XXXI", "&b", 31, 920000, 120, 4.1),
    THIRTY_TWO("XXXII", "&b", 32, 960000, 120, 4.2),
    THIRTY_THREE("XXXIII", "&b", 33, 1200000, 125, 4.5),
    THIRTY_FOUR("XXXIV", "&b", 34, 1204000, 125, 4.8),
    THIRTY_FIVE("XXV", "&b", 35, 2000000, 130, 5);

    public final String name;
    public final String color;
    private final int integerValue;
    private final BigDecimal price;
    private final BigDecimal renownReward;
    private final double xpNeededMultiplier;

    Prestige(String name, String color, int integerValue, double price, double renownReward, double xpNeededMultiplier) {
        this.name = name;
        this.color = color;
        this.integerValue = integerValue;
        this.price = new BigDecimal(price);
        this.renownReward = new BigDecimal(renownReward);
        this.xpNeededMultiplier = xpNeededMultiplier;
    }

    public String getName() {
        return "§e" + name;
    }

    public String getColor() {
        return ChatColor.translateAlternateColorCodes('&', color);
    }

    public int getIntegerValue() {
        return integerValue;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getRenownReward() {
        return renownReward;
    }

    public double getXpNeededMultiplier() {
        return xpNeededMultiplier;
    }

    public Prestige getNextPrestige() {
        if (values()[values().length - 1] == this) {
            return values()[0];
        }

        for (int i = 0; i < values().length; i++) {
            Prestige current = values()[i];
            if (current == this) {
                return values()[i + 1];
            }
        }

        return null;
    }

    public static Prestige getByName(String name) {
        return Arrays.stream(values()).filter(prestige -> prestige.name.equalsIgnoreCase(name)).findFirst().orElse(ZERO);
    }
}
