package fr.maximouz.thepit.statistic;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerPlaySession {

    private final UUID playerUniqueId;
    private final long start;
    private final long end;

    public PlayerPlaySession(UUID playerUniqueId, long start, long end) {
        this.playerUniqueId = playerUniqueId;
        this.start = start;
        this.end = end;
    }

    public UUID getPlayerUniqueId() {
        return playerUniqueId;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getTimePlayed() {
        return TimeUnit.MILLISECONDS.toSeconds(getEnd() - getStart());
    }

}
