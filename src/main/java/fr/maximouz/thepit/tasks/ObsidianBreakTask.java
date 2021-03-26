package fr.maximouz.thepit.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ObsidianBreakTask extends BukkitRunnable {

    private static final List<ObsidianBreakTask> tasks = new ArrayList<>();

    private final Location location;

    public ObsidianBreakTask(Location location) {
        tasks.add(this);
        this.location = location;
    }

    @Override
    public void run() {
        location.getBlock().setType(Material.AIR);
    }

    public Location getLocation() {
        return location;
    }

    public static ObsidianBreakTask getTask(Location location) {
        return tasks.stream().filter(task -> task.getLocation() == location).findFirst().orElse(null);
    }

}
