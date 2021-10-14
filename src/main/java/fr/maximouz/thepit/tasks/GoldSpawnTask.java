package fr.maximouz.thepit.tasks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.area.AreaManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GoldSpawnTask extends BukkitRunnable {

    private final static GoldSpawnTask INSTANCE = new GoldSpawnTask();

    private boolean started;

    public GoldSpawnTask() {
        started = false;
    }

    public static GoldSpawnTask getInstance() {
        return INSTANCE;
    }

    public boolean hasStarted() {
        return started;
    }

    @Override
    public void run() {

        started = true;

        if (Bukkit.getOnlinePlayers().size() > 0) {

            Location location = AreaManager.getInstance().getMapArea().getCuboid().getRandomLocation();
            Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> location.getWorld().dropItem(location, new ItemStack(Material.GOLD_INGOT)));

        }

    }

}
