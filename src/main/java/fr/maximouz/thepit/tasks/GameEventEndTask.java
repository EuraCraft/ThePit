package fr.maximouz.thepit.tasks;

import fr.maximouz.thepit.ThePit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEventEndTask extends BukkitRunnable {

    private long endTime;

    public GameEventEndTask(long endTime) {
        this.endTime = endTime;
    }

    public GameEventEndTask start() {
        runTaskTimerAsynchronously(ThePit.getInstance(), 0L, 20L);
        return this;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        if (endTime != -1L && now >= endTime) {
            endTime = -1L;
            ThePit.getInstance().getGameEventManager().endCurrentGame();
        }
    }
}
