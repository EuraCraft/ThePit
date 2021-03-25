package fr.maximouz.thepit.statistic;

import org.bukkit.ChatColor;

public enum PlayerStatus {

    IDLING("&aEn attente"),
    FIGHTING("&cEn combat");

    private final String string;

    PlayerStatus(String string) {
        this.string = string;
    }

    public String toString(boolean colored) {
        return colored ? ChatColor.translateAlternateColorCodes('&', string) : string;
    }

    public String toString() {
        return toString(true);
    }

}
