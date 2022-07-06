package fr.maximouz.thepit.gameevent;

import fr.maximouz.thepit.gameevent.events.TeamFightGameEvent;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GameEventType {

    TEAM_FIGHT(TeamFightGameEvent.class, 2),
    CAPTURE_THE_FLAGS(TeamFightGameEvent.class, 1),
    GOLD_RAIN(TeamFightGameEvent.class, 1);

    private final Class<? extends GameEvent> clazz;
    private final int minPlayers;

    GameEventType(Class<? extends GameEvent> clazz, int minPlayers) {
        this.clazz = clazz;
        this.minPlayers = minPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public static List<GameEventType> getAvailableGames() {
        int online = Bukkit.getOnlinePlayers().size();
        return Arrays.stream(values())
                .filter(type -> type.getMinPlayers() <= online)
                .collect(Collectors.toList());
    }

    public GameEvent newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
