package fr.maximouz.thepit.tasks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.gameevent.GameEventType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameEventTask extends BukkitRunnable {

    private final long startTime;
    private GameEventType gameEventType;

    public GameEventTask(long startTime, GameEventType gameEventType) {
        this.startTime = startTime;
        this.gameEventType = gameEventType;
    }

    public long getStartTime() {
        return startTime;
    }

    public GameEventTask start() {
        runTaskTimerAsynchronously(ThePit.getInstance(), 0L, 20L);
        return this;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        if (startTime >= now) {
            if (ThePit.getInstance().getGameEventManager().getCurrentGame() != null)
                return;
            if (gameEventType.getMinPlayers() > Bukkit.getOnlinePlayers().size()) {
                List<GameEventType> availableTypes = GameEventType.getAvailableGames();
                if (availableTypes.size() <= 0) {
                    cancel();
                    return;
                }
                gameEventType = availableTypes.get(ThreadLocalRandom.current().nextInt(availableTypes.size()));
            }
            ThePit.getInstance().getGameEventManager().start(startTime, gameEventType);
            cancel();
        }
    }
}
