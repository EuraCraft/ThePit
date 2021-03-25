package fr.maximouz.thepit.statistic;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerStatistic {

    private final Player player;
    private final List<PlayerKill> kills;
    private final List<PlayerDeath> deaths;
    private final List<PlayerPlaySession> playSessions;

    private long lastDamage;
    private long killStreak;

    public PlayerStatistic(Player player) {
        this.player = player;
        this.kills = new ArrayList<>();
        this.deaths = new ArrayList<>();
        this.playSessions = new ArrayList<>();
        this.lastDamage = System.currentTimeMillis() - 160000;
    }


    public Player getPlayer() {
        return player;
    }

    public List<PlayerKill> getKills() {
        return kills;
    }

    public void addKill(PlayerKill kill) {
        this.kills.add(kill);
    }

    public void addKill(UUID victim, long date) {
        addKill(new PlayerKill(player.getUniqueId(), victim, date));
    }

    public void addDeath(PlayerDeath death) {
        this.deaths.add(death);
    }

    public void addDeath(UUID killer, long date) {
        addDeath(new PlayerDeath(player.getUniqueId(), killer, date));
    }

    public List<PlayerPlaySession> getPlaySessions() {
        return playSessions;
    }

    public void addPlaySession(PlayerPlaySession playSession) {
        this.playSessions.add(playSession);
    }

    public void addPlayerSession(long start, long end) {
        addPlaySession(new PlayerPlaySession(player.getUniqueId(), start, end));
    }

    public boolean hasDisconnectSince(long date) {
        return playSessions.stream().anyMatch(playSession ->  playSession.getEnd() >= date);
    }

    public List<PlayerKill> getKillsSince(long date) {
        return kills.stream().filter(kill -> kill.getDate() >= date).collect(Collectors.toList());
    }

    public void updateKillStreak() {

        PlayerDeath lastDeath = getLastDeath();
        long lastDeathDate = lastDeath != null ? lastDeath.getDate() : -1;

        this.killStreak = getKillsSince(lastDeathDate).stream()
                .filter(kill -> !hasDisconnectSince(kill.getDate()))
                .count();
    }

    public long getKillStreak() {
        return killStreak;
    }

    public PlayerStatus getStatus() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastDamage) >= 15 ? PlayerStatus.IDLING : PlayerStatus.FIGHTING;
    }

    public void damage() {
        this.lastDamage = System.currentTimeMillis();
    }

    public long getLastDamage() {
        return lastDamage;
    }

    public void cancelLastDamage() {
        this.lastDamage = System.currentTimeMillis() - 160000;
    }

    public long getFightingTimeLeft() {
        return Math.abs((System.currentTimeMillis() - lastDamage) - 15000);
    }

    public PlayerDeath getLastDeath() {

        PlayerDeath lastDeath = null;

        if (deaths.size() > 0) {

            lastDeath = deaths.stream().max(Comparator.comparingLong(PlayerDeath::getDate)).get();


        }

        return lastDeath;

    }

}
