package fr.maximouz.thepit.area;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.utils.Configuration;
import fr.maximouz.thepit.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class AreaManager {

    private static final AreaManager INSTANCE = new AreaManager();

    private final Configuration configuration;
    private final List<Area> areas;
    private Area mapArea;

    public AreaManager() {
        areas = new ArrayList<>();
        configuration = new Configuration(ThePit.getInstance(), "areas");
        configuration.saveDefaultConfig();
    }

    public static AreaManager getInstance() {
        return INSTANCE;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public Area getArea(String name) {
        return areas.stream()
                .filter(area -> area.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void loadAreas() {

        int max = 0;
        int loaded = 0;
        Bukkit.getLogger().info("Loading areas..");
        for (String areaName : configuration.getConfig().getConfigurationSection("").getKeys(false)) {
            try {

                if (areaName.equalsIgnoreCase("map")) {

                    Cuboid cuboid = Cuboid.fromString(configuration.getConfig().getString(areaName));
                    mapArea = new Area(cuboid, areaName, "");

                } else {

                    Cuboid cuboid = Cuboid.fromString(configuration.getConfig().getString(areaName + ".cuboid"));
                    String displayName = configuration.getConfig().getString(areaName + ".display_name");
                    areas.add(new Area(cuboid, areaName, displayName));

                }

                Bukkit.getLogger().info("Loaded area " + areaName);
                loaded++;

            } catch (Exception ignored) {}

            max++;
        }

        Bukkit.getLogger().info("Areas loaded. (" + loaded + "/" + max + ")");

    }

    public void saveAreas() {

        configuration.getConfig().getConfigurationSection("").getKeys(false).forEach(areaName -> configuration.getConfig().set(areaName, null));
        areas.forEach(area -> area.save(configuration));

    }

    public Area getMapArea() {
        return mapArea;
    }
}
