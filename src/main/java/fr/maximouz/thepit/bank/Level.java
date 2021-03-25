package fr.maximouz.thepit.bank;

import org.bukkit.ChatColor;

public enum Level {

    // 1 - 9
    ONE(0, 1, "&7", "119"),
    TWO(5, 2, "&7", "118"),
    THREE(10, 3, "&7", "117"),
    FOUR(15, 4, "&7", "116"),
    FIVE(20, 5, "&7", "115"),
    SIX(25, 6, "&7", "114"),
    SEVEN(30, 7, "&7", "113"),
    EIGHT(35, 8, "&7", "112"),
    NINE(40, 9, "&7", "111"),

    // 10 - 19
    TEN(45, 10, "&9", "110"),
    ELEVEN(50, 11, "&9", "109"),
    TWELVE(55, 12, "&9", "108"),
    THIRTEEN(60, 13, "&9", "107"),
    FOURTEEN(65, 14, "&9", "106"),
    FIFTEEN(70, 15, "&9", "105"),
    SIXTEEN(75, 16, "&9", "104"),
    SEVENTEEN(80, 17, "&9", "103"),
    EIGHTEEN(85, 18, "&9", "102"),
    NINETEEN(90, 19, "&9", "101"),

    // 20 - 29
    TWENTY(95, 20, "&3", "100"),
    TWENTY_ONE(10, 21, "&3", "099"),
    TWENTY_TWO(105, 22, "&3", "098"),
    TWENTY_THREE(110, 23, "&3", "097"),
    TWENTY_FOUR(115, 24, "&3", "096"),
    TWENTY_FIVE(120, 25, "&3", "095"),
    TWENTY_SIX(125, 26, "&3", "094"),
    TWENTY_SEVEN(130, 27, "&3", "093"),
    TWENTY_EIGHT(135, 28, "&3", "092"),
    TWENTY_NINE(140, 29, "&3", "091"),

    // 30 - 39
    THIRTY(145, 30, "&2", "090"),
    THIRTY_ONE(150, 31, "&2", "089"),
    THIRTY_TWO(155, 32, "&2", "088"),
    THIRTY_THREE(160, 33, "&2", "087"),
    THIRTY_FOUR(165, 34, "&2", "086"),
    THIRTY_FIVE(170, 35, "&2", "085"),
    THIRTY_SIX(175, 36, "&2", "084"),
    THIRTY_SEVEN(180, 37, "&2", "083"),
    THIRTY_EIGHT(185, 38, "&2", "082"),
    THIRTY_NINE(190, 39, "&2", "081"),

    // 40 - 49
    FORTY(195, 40, "&a", "080"),
    FORTY_ONE(200, 41, "&a", "079"),
    FORTY_TWO(205, 42, "&a", "078"),
    FORTY_THREE(210, 43, "&a", "077"),
    FORTY_FOUR(215, 44, "&a", "076"),
    FORTY_FIVE(220, 45, "&a", "075"),
    FORTY_SIX(225, 46, "&a", "074"),
    FORTY_SEVEN(230, 47, "&a", "073"),
    FORTY_EIGHT(235, 48, "&a", "072"),
    FORTY_NINE(240, 49, "&a", "071"),

    // 50 - 59
    FIFTY(245, 50, "&e", "070"),
    FIFTY_ONE(250, 51, "&e", "069"),
    FIFTY_TWO(255, 52, "&e", "068"),
    FIFTY_THREE(260, 53, "&e", "067"),
    FIFTY_FOUR(265, 54, "&e", "066"),
    FIFTY_FIVE(270, 55, "&e", "065"),
    FIFTY_SIX(275, 56, "&e", "064"),
    FIFTY_SEVEN(280, 57, "&e", "063"),
    FIFTY_EIGHT(285, 58, "&e", "062"),
    FIFTY_NINE(290, 59, "&e", "061"),

    // 60 - 69
    SIXTY(290, 60, "&6&l", "060"),
    SIXTY_ONE(295, 61, "&6&l", "059"),
    SIXTY_TWO(300, 62, "&6&l", "058"),
    SIXTY_THREE(305, 63, "&6&l", "057"),
    SIXTY_FOUR(310, 64, "&6&l", "056"),
    SIXTY_FIVE(315, 65, "&6&l", "055"),
    SIXTY_SIX(320, 66, "&6&l", "054"),
    SIXTY_SEVEN(325, 67, "&6&l", "053"),
    SIXTY_EIGHT(330, 68, "&6&l", "052"),
    SIXTY_NINE(335, 69, "&6&l", "051"),

    // 70 - 79
    SEVENTY(340, 70, "&c&l", "050"),
    SEVENTY_ONE(345, 71, "&c&l", "049"),
    SEVENTY_TWO(350, 72, "&c&l", "048"),
    SEVENTY_THREE(355, 73, "&c&l", "047"),
    SEVENTY_FOUR(360, 74, "&c&l", "046"),
    SEVENTY_FIVE(365, 75, "&c&l", "045"),
    SEVENTY_SIX(370, 76, "&c&l", "044"),
    SEVENTY_SEVEN(375, 77, "&c&l", "043"),
    SEVENTY_EIGHT(380, 78, "&c&l", "042"),
    SEVENTY_NINE(385, 79, "&c&l", "041"),

    // 80 - 89
    EIGHTY(340, 80, "&4&l", "040"),
    EIGHTY_ONE(345, 81, "&4&l", "039"),
    EIGHTY_TWO(350, 82, "&4&l", "038"),
    EIGHTY_THREE(355, 83, "&4&l", "037"),
    EIGHTY_FOUR(360, 84, "&4&l", "036"),
    EIGHTY_FIVE(365, 85, "&4&l", "035"),
    EIGHTY_SIX(370, 86, "&4&l", "034"),
    EIGHTY_SEVEN(375, 87, "&4&l", "033"),
    EIGHTY_EIGHT(380, 88, "&4&l", "032"),
    EIGHTY_NINE(385, 89, "&4&l", "031"),

    // 90 - 99
    NINETY(390, 90, "&5&l", "030"),
    NINETY_ONE(395, 91, "&5&l", "029"),
    NINETY_TWO(400, 92, "&5&l", "028"),
    NINETY_THREE(405, 93, "&5&l", "027"),
    NINETY_FOUR(410, 94, "&5&l", "026"),
    NINETY_FIVE(415, 95, "&5&l", "025"),
    NINETY_SIX(420, 96, "&5&l", "024"),
    NINETY_SEVEN(425, 97, "&5&l", "023"),
    NINETY_EIGHT(430, 98, "&5&l", "022"),
    NINETY_NINE(435, 99, "&5&l", "021"),

    // 100 - 109
    ONE_HUNDRED(440, 100, "&d&l", "020"),
    ONE_HUNDRED_AND_ONE(445, 101, "&d&l", "019"),
    ONE_HUNDRED_AND_TWO(450, 102, "&d&l", "018"),
    ONE_HUNDRED_AND_THREE(455, 103, "&d&l", "017"),
    ONE_HUNDRED_AND_FOUR(460, 104, "&d&l", "016"),
    ONE_HUNDRED_AND_FIVE(465, 105, "&d&l", "015"),
    ONE_HUNDRED_AND_SIX(470, 106, "&d&l", "014"),
    ONE_HUNDRED_AND_SEVEN(475, 107, "&d&l", "013"),
    ONE_HUNDRED_AND_EIGHT(480, 108, "&d&l", "012"),
    ONE_HUNDRED_AND_NINE(485, 109, "&d&l", "011"),

    // 110 - 119
    ONE_HUNDRED_AND_TEN(490, 110, "&f&l", "010"),
    ONE_HUNDRED_AND_ELEVEN(495, 111, "&f&l", "009"),
    ONE_HUNDRED_AND_TWELVE(500, 112, "&f&l", "008"),
    ONE_HUNDRED_AND_THIRTEEN(505, 113, "&f&l", "007"),
    ONE_HUNDRED_AND_FOURTEEN(510, 114, "&f&l", "006"),
    ONE_HUNDRED_AND_FIFTEEN(515, 115, "&f&l", "005"),
    ONE_HUNDRED_AND_SIXTEEN(520, 116, "&f&l", "004"),
    ONE_HUNDRED_AND_SEVENTEEN(525, 117, "&f&l", "003"),
    ONE_HUNDRED_AND_EIGHTEEN(530, 118, "&f&l", "002"),
    ONE_HUNDRED_AND_NINETEEN(540, 119, "&f&l", "001"),

    // 120
    ONE_HUNDRED_AND_TWENTY(545, 120, "&b&l", "000");

    public final double expRequired;
    public final int level;
    public final String color;
    public final String priority;

    Level(double expRequired, int level, String color, String priority) {
        this.expRequired = expRequired;
        this.level = level;
        this.color = color;
        this.priority = priority;
    }

    public static Level getLevel(int level) {

        if (values()[values().length - 1].level <= level)
            return values()[values().length - 1];

        for (int i = 0; i < values().length; i++) {
            if (values()[i].level == level)
                return values()[i];
        }

        return ONE;
    }

    public String getColor() {
        return ChatColor.translateAlternateColorCodes('&', color);
    }

    public String getLevel() {
        return getColor() + level;
    }

    public String getPriority() {
        return priority;
    }
}
