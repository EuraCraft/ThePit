package fr.maximouz.thepit.statistic;

import java.util.UUID;

public class PlayerDeath {

    private final UUID player;
    private final UUID killer;
    private final long date;

    public PlayerDeath(UUID player, UUID killer, long date) {
        this.player = player;
        this.killer = killer;
        this.date = date;
    }

    public UUID getPlayer() {
        return player;
    }

    public UUID getKiller() {
        return killer;
    }

    public long getDate() {
        return date;
    }

}
