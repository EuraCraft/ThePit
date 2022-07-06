package fr.maximouz.thepit.gameevent;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.tasks.GameEventEndTask;
import fr.maximouz.thepit.tasks.GameEventTask;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameEventManager {

    private final Map<Long, GameEventType> eventsCalendar;
    private GameEvent currentGame;
    private GameEventEndTask currentGameEndTask;

    private final List<GameEventTask> tasks;

    public GameEventManager() {
        eventsCalendar = new HashMap<>();
        tasks = new ArrayList<>();
        generateEvents(Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Paris"))));
    }

    public Map<Long, GameEventType> getEventsCalendar() {
        return eventsCalendar;
    }

    public GameEvent getCurrentGame() {
        return currentGame;
    }

    public List<GameEventTask> getTasks() {
        return tasks;
    }

    public void removeTask(long startTime) {
        getTasks().stream()
                .filter(task -> task.getStartTime() == startTime)
                .forEach(BukkitRunnable::cancel);
        getTasks().removeIf(task -> task.getStartTime() == startTime);
    }

    public void start(Long startTime, GameEventType gameEventType) {
        removeTask(startTime);
        eventsCalendar.remove(startTime);
        if (eventsCalendar.isEmpty()) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            generateEvents(calendar);
        }
        if (currentGame != null)
            HandlerList.unregisterAll(currentGame);
        currentGame = gameEventType.newInstance();
        Bukkit.getPluginManager().registerEvents(currentGame, ThePit.getInstance());
        currentGame.start();
        currentGameEndTask = new GameEventEndTask(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).start();
    }

    public void startNextGame() {
        getEventsCalendar().entrySet().stream()
                .min(Comparator.comparingLong(Map.Entry::getKey))
                .ifPresent(entry -> start(entry.getKey(), entry.getValue()));
    }

    public void endCurrentGame() {
        HandlerList.unregisterAll(currentGame);
        currentGame.end();
        currentGame = null;
        currentGameEndTask.cancel();
        currentGameEndTask = null;
    }

    private void generateEvents(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        for (int i = calendar.get(Calendar.HOUR_OF_DAY); i < 24; i++) {
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), i, 0, 0);
            if (cal.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH))
                break;
            long min = cal.getTime().getTime();
            cal.add(Calendar.HOUR_OF_DAY, 1);
            long max = cal.getTime().getTime();
            long eventTime = ThreadLocalRandom.current().nextLong(min, max);
            GameEventType type = GameEventType.values()[ThreadLocalRandom.current().nextInt(GameEventType.values().length)];
            eventsCalendar.put(eventTime, type);
            tasks.add(new GameEventTask(eventTime, type).start());
        }
    }
}
