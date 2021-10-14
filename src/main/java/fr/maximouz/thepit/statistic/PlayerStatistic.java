package fr.maximouz.thepit.statistic;

import fr.maximouz.thepit.ThePit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerStatistic {

    private final Player player;
    private long kills;
    private long assists;
    private long deaths;
    private long killStreak;
    private final List<PlayerPlaySession> playSessions;
    private final long connectedAt;

    private final Map<Player, Double> damages;
    private final Map<Player, List<Material>> damageItemsUsed;

    private long lastAttackDate;
    private BukkitTask fightingEndTask;

    public PlayerStatistic(Player player) {
        this.player = player;
        kills = 0L;
        assists = 0L;
        deaths = 0L;
        killStreak = 0L;
        playSessions = new ArrayList<>();
        lastAttackDate = System.currentTimeMillis() - 160000;
        damages = new HashMap<>();
        damageItemsUsed = new HashMap<>();
        fightingEndTask = null;
        connectedAt = System.currentTimeMillis();
    }


    public Player getPlayer() {
        return player;
    }

    public long getKills() {
        return kills;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public long getAssists() {
        return assists;
    }

    public void setAssists(long assists) {
        this.assists = assists;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public long getKillStreak() {
        return killStreak;
    }

    public void setKillStreak(long killStreak) {
        this.killStreak = killStreak;
    }

    public List<PlayerPlaySession> getPlaySessions() {
        return playSessions;
    }

    public void addPlaySession(PlayerPlaySession playSession) {
        this.playSessions.add(playSession);
    }

    public void addPlaySession(long start, long end) {
        addPlaySession(new PlayerPlaySession(player.getUniqueId(), start, end));
    }

    public long getConnectedAt() {
        return connectedAt;
    }

    public void damage(Player player, double damage, Material itemUsed) {
        damages.put(player, getDamage(player) + damage);
        List<Material> materials = damageItemsUsed.getOrDefault(player, new ArrayList<>());
        materials.add(itemUsed);
        damageItemsUsed.put(player, materials);
    }

    public double getDamage(Player player) {
        return damages.getOrDefault(player, 0.0);
    }

    public List<Material> getDamageItemUsed(Player player) {
        return damageItemsUsed.getOrDefault(player, new ArrayList<>());
    }

    public void resetDamage(Player target) {
        damages.remove(target);
        damageItemsUsed.remove(target);
    }

    public boolean hasDisconnectSince(long date) {
        return playSessions.stream().anyMatch(playSession ->  playSession.getEnd() >= date);
    }

    public PlayerStatus getStatus() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastAttackDate) >= 15 ? PlayerStatus.IDLING : PlayerStatus.FIGHTING;
    }

    public void setLastAttackDate(long lastAttackDate) {
        this.lastAttackDate = lastAttackDate;
        if (fightingEndTask != null)
            fightingEndTask.cancel();
        fightingEndTask = Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
            PlayerStatisticsManager.getInstance().removeAssistsDamage(player);
            fightingEndTask = null;
        }, 20 * TimeUnit.MILLISECONDS.toSeconds(getFightingTimeLeft()));
    }

    public long getLastAttackDate() {
        return lastAttackDate;
    }

    public void cancelLastDamage() {
        lastAttackDate = System.currentTimeMillis() - 160000;
    }

    public long getFightingTimeLeft() {
        return Math.abs((System.currentTimeMillis() - getLastAttackDate()) - 15000);
    }


}
