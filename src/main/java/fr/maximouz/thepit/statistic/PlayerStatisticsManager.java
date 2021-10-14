package fr.maximouz.thepit.statistic;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatisticsManager {

    private static final PlayerStatisticsManager INSTANCE = new PlayerStatisticsManager();

    private final List<PlayerStatistic> playerStatistics;

    public PlayerStatisticsManager() {
        this.playerStatistics = new ArrayList<>();
    }

    public static PlayerStatisticsManager getInstance() {
        return INSTANCE;
    }

    public List<PlayerStatistic> getPlayerStatistics() {
        return playerStatistics;
    }

    public void loadPlayerStatistic(Player player) {
        this.playerStatistics.add(new PlayerStatistic(player));
    }

    public void savePlayerStatistic(PlayerStatistic playerStatistic) {
        this.playerStatistics.remove(playerStatistic);
    }

    public void savePlayerStatistic(Player player) {
        savePlayerStatistic(getPlayerStatistic(player));
    }

    public PlayerStatistic getPlayerStatistic(Player player) {
        return playerStatistics.stream()
                .filter(playerStatistic -> playerStatistic.getPlayer().getUniqueId() == player.getUniqueId())
                .findFirst()
                .orElse(null);
    }

    public double getDamageTaken(Player player) {
        double damage = 0;

        for (PlayerStatistic playerStatistic : playerStatistics) {

            if (playerStatistic.getPlayer() != player) {

                damage += playerStatistic.getDamage(player);
            }

        }

        return damage;
    }

    public void removeAssistsDamage(PlayerStatistic playerStatistic) {

        playerStatistics.forEach(statistic -> {

            if (statistic != playerStatistic)
                statistic.resetDamage(playerStatistic.getPlayer());

        });

    }

    public void removeAssistsDamage(Player player) {
        removeAssistsDamage(getPlayerStatistic(player));
    }

    /*public long getLastDeathDate(Player player) {

        PlayerKill lastKill = null;

        for (PlayerStatistic playerStatistic : playerStatistics) {

            for (PlayerKill playerKill : playerStatistic.getKills()) {

                if (playerKill.getVictim() == player.getUniqueId())
                    lastKill = lastKill == null ? playerKill : lastKill.getDate() > playerKill.getDate() ? playerKill : lastKill;

            }

        }

        return lastKill != null ? lastKill.getDate() : -1;

    }*/

}
