package fr.maximouz.thepit.scoreboard;

import fr.euracraft.api.player.IEuraPlayer;
import fr.maximouz.thepit.ThePit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
public class ScoreboardManager {

    private static final ScoreboardManager INSTANCE = new ScoreboardManager();

    private final Map<UUID, PersonalScoreboard> scoreboards;
    private final BukkitTask updateTask;

    public ScoreboardManager() {
        scoreboards = new HashMap<>();

        updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(ThePit.getInstance(), () -> scoreboards.values().forEach(PersonalScoreboard::updateScoreBoard), 0L, 20L);

    }

    public static ScoreboardManager getInstance() {
        return INSTANCE;
    }

    public void disable() {
        updateTask.cancel();
    }

    public void register(IEuraPlayer euraPlayer) {
        scoreboards.put(euraPlayer.getPlayer().getUniqueId(), new PersonalScoreboard(euraPlayer.getPlayer()));
    }

    public void remove(Player player) {
        PersonalScoreboard scoreboard = scoreboards.get(player.getUniqueId());
        scoreboards.remove(player.getUniqueId());
        scoreboard.getEuraScoreboard().destroy();
    }

    public static void updateScoreBoard(IEuraPlayer euraPlayer) {
        updateScoreBoard(euraPlayer.getPlayer());
    }

    public static void updateScoreBoard(Player player) {
        updateScoreBoard(player.getUniqueId());
    }

    public static void updateScoreBoard(UUID uuid) {
        INSTANCE.scoreboards.get(uuid).updateScoreBoard();
    }

}
