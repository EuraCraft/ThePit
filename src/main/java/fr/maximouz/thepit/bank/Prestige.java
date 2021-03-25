package fr.maximouz.thepit.bank;

import org.bukkit.ChatColor;

import java.util.Arrays;

public enum Prestige {

    // 0 - 9
    ZERO("", "&7", "39"),
    ONE("I", "&3", "38"),
    TWO("II", "&3", "37"),
    THREE("III", "&3", "36"),
    FOUR("IV", "&3", "35"),
    FIVE("V", "&e", "34"),
    SIX("VI", "&e", "33"),
    SEVEN("VII", "&e", "32"),
    EIGHT("VIII", "&e", "31"),
    NINE("IX", "&e", "30"),

    // 10 - 19
    TEN("X", "&6", "29"),
    ELEVEN("XI", "&6", "28"),
    TWELVE("XII", "&6", "27"),
    THIRTEEN("XIII", "&6", "26"),
    FOURTEEN("XIV", "&6", "25"),
    FIFTEEN("XV", "&c", "24"),
    SIXTEEN("XVI", "&c", "23"),
    SEVENTEEN("XVII", "&c", "22"),
    EIGHTEEN("XVIII", "&c", "21"),
    NINETEEN("XIX", "&c", "20"),

    // 20 - 29
    TWENTY("XX", "&5", "19"),
    TWENTY_ONE("XXI", "&5", "18"),
    TWENTY_TWO("XXII", "&5", "17"),
    TWENTY_THREE("XXIII", "&5", "16"),
    TWENTY_FOUR("XXIV", "&5", "15"),
    TWENTY_FIVE("XXV", "&d", "14"),
    TWENTY_SIX("XXVI", "&d", "13"),
    TWENTY_SEVEN("XXVII", "&d", "12"),
    TWENTY_EIGHT("XXVIII", "&d", "11"),
    TWENTY_NINE("XXIX", "&d", "10"),

    // 30 - 35
    THIRTY("XXX", "&b", "05"),
    THIRTY_ONE("XXXI", "&b", "04"),
    THIRTY_TWO("XXXII", "&b", "03"),
    THIRTY_THREE("XXXIII", "&b", "02"),
    THIRTY_FOUR("XXXIV", "&b", "01"),
    THIRTY_FIVE("XXV", "&b", "00");

    public final String name;
    public final String color;
    public final String priority;

    Prestige(String name, String color, String priority) {
        this.name = name;
        this.color = color;
        this.priority = priority;
    }


    public String getName() {
        return ChatColor.YELLOW + name;
    }

    public String getColor() {
        return ChatColor.translateAlternateColorCodes('&', color);
    }

    public Prestige getNextPrestige(Prestige previous) {
        if (values()[values().length - 1] == previous) {
            return values()[0];
        }

        for (int i = 0; i < values().length; i++) {
            Prestige current = values()[i];
            if (current == previous) {
                return values()[i + 1];
            }
        }

        return null;
    }

    public static Prestige getByName(String name) {
        return Arrays.stream(values()).filter(prestige -> prestige.name.equalsIgnoreCase(name)).findFirst().orElse(ZERO);
    }

    public String getPriority() {
        return priority;
    }
}
