package fr.maximouz.thepit.statistic;

import org.bukkit.Material;

import java.util.UUID;

public class PlayerAssist {

    private final UUID player;
    private final UUID victim;
    private final long date;
    private final double damage;

    private final Material weaponUsed;

    public PlayerAssist(UUID player, UUID victim, long date, double damage, Material weaponUsed) {
        this.player = player;
        this.victim = victim;
        this.date = date;
        this.damage = damage;
        this.weaponUsed = weaponUsed;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getVictim() {
        return victim;
    }

    public long getDate() {
        return date;
    }

    public double getDamage() {
        return damage;
    }

    public Material getWeaponUsed() {
        return weaponUsed;
    }
}
