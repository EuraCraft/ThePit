package fr.maximouz.thepit.bank;

import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Arrays;

public enum Prestige {

    // 0 - 9
    ZERO("", "&7", 0, 0, 0, 1),
    ONE("I", "&3", 1, 10000, 10, 10),
    TWO("II", "&3", 2, 22000, 10, 20),
    THREE("III", "&3", 3, 24000, 10, 30),
    FOUR("IV", "&3", 4, 26000, 10, 40),
    FIVE("V", "&e", 5, 28000, 20, 50),
    SIX("VI", "&e", 6, 30000, 20, 75),
    SEVEN("VII", "&e", 7, 70000, 20, 100),
    EIGHT("VIII", "&e", 8, 80000, 20, 150),
    NINE("IX", "&e", 9, 100000, 20, 200),

    // 10 - 19
    TEN("X", "&6", 10, 120000, 20, 300),
    ELEVEN("XI", "&6", 11, 120000, 30, 400),
    TWELVE("XII", "&6", 12, 200000, 30, 500),
    THIRTEEN("XIII", "&6", 13, 240000, 30, 600),
    FOURTEEN("XIV", "&6", 14, 280000, 30, 700),
    FIFTEEN("XV", "&c", 15, 320000, 40, 800),
    SIXTEEN("XVI", "&c",  16, 360000, 40, 900),
    SEVENTEEN("XVII", "&c", 17, 440000, 50, 1100),
    EIGHTEEN("XVIII", "&c", 18, 480000, 50, 1300),
    NINETEEN("XIX", "&c", 19),

    // 20 - 29
    TWENTY("XX", "&5", 20),
    TWENTY_ONE("XXI", "&5", 21),
    TWENTY_TWO("XXII", "&5", 22),
    TWENTY_THREE("XXIII", "&5", 23),
    TWENTY_FOUR("XXIV", "&5", 24),
    TWENTY_FIVE("XXV", "&d", 25),
    TWENTY_SIX("XXVI", "&d", 26),
    TWENTY_SEVEN("XXVII", "&d", 27),
    TWENTY_EIGHT("XXVIII", "&d", 28),
    TWENTY_NINE("XXIX", "&d", 29),

    // 30 - 35
    THIRTY("XXX", "&b", 30),
    THIRTY_ONE("XXXI", "&b", 31),
    THIRTY_TWO("XXXII", "&b", 32),
    THIRTY_THREE("XXXIII", "&b", 33),
    THIRTY_FOUR("XXXIV", "&b", 34),
    THIRTY_FIVE("XXV", "&b", 35);

    public final String name;
    public final String color;
    private final int integerValue;
    private final BigDecimal price;
    private final BigDecimal renownReward;
    private final int xpNeededMultiplier;

    Prestige(String name, String color, int integerValue, double price, double renownReward, int xpNeededMultiplier) {
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

    public int getXpNeededMultiplier() {
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
