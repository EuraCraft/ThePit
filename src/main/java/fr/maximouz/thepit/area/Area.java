package fr.maximouz.thepit.area;

import fr.maximouz.thepit.utils.Configuration;
import fr.maximouz.thepit.utils.Cuboid;
import org.bukkit.ChatColor;

public class Area {

    private final Cuboid cuboid;
    private final String name;
    private final String displayName;

    public Area(Cuboid cuboid, String name, String displayName) {
        this.cuboid = cuboid;
        this.name = name;
        this.displayName = displayName;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', displayName);
    }

    public void save(Configuration configuration) {

        configuration.getConfig().set(getName() + ".display_name", getDisplayName());
        configuration.getConfig().set(getName() + ".cuboid", cuboid.toString());

    }
}
