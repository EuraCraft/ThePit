package fr.maximouz.thepit.statistic;

import java.util.UUID;

public class PlayerKill {

    private final UUID player;
    private final UUID victim;
    private final long date;

    public PlayerKill(UUID player, UUID victim, long date) {
        this.player = player;
        this.victim = victim;
        this.date = date;
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

}
