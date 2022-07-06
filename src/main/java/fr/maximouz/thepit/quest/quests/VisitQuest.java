package fr.maximouz.thepit.quest.quests;

import fr.maximouz.thepit.area.Area;
import fr.maximouz.thepit.events.PlayerChangeAreaEvent;
import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.*;

public class VisitQuest extends Quest {

    private final Map<Area, List<UUID>> visited;

    public VisitQuest(QuestType type) {
        super(type);
        visited = new HashMap<>();
    }

    private void visit(Player player, Area area) {
        List<UUID> uuids = visited.getOrDefault(area, new ArrayList<>());
        if (!uuids.contains(player.getUniqueId()))
            uuids.add(player.getUniqueId());
        visited.put(area, uuids);
    }

    private boolean hasVisited(Player player, Area area) {
        return visited.getOrDefault(area, new ArrayList<>()).contains(player.getUniqueId());
    }

    @EventHandler
    public void onAreaChange(PlayerChangeAreaEvent event) {

        Player player = event.getPlayer();
        Area area = event.getAreaTo();
        if (!hasVisited(player, area)) {
            visit(player, area);
            increaseProgression(player);
        }

    }

    @Override
    public void onReset() {
        visited.clear();
    }

}
