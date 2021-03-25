package fr.maximouz.thepit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class WorldUtils {

    /**
     * Obtenir une Location à partir d'une chaîne de caractère en config
     * @param config The configuration file where is located the string location
     * @param path The path to the string location
     * @return Location or null if the string location is corrupted
     */
    public static Location loadLocation(FileConfiguration config, String path) {
        String worldName = config.getString(path + ".world");
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            try {
                double x = config.getDouble(path + ".x");
                double y = config.getDouble(path + ".y");
                double z = config.getDouble(path + ".z");
                float yaw = 0f;
                float pitch = 0f;

                if (config.contains(path + ".yaw"))
                    yaw = Float.parseFloat(config.getString(path + ".yaw"));

                if (config.contains(path + ".pitch"))
                    pitch = Float.parseFloat(config.getString(path + ".pitch"));

                return new Location(world, x, y, z, yaw, pitch);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Obtenir une Location à partir d'une chaîne de caractère en config avec un World choisi au préalable
     * @param config The configuration file where is located the string location
     * @param path The path to the string location
     * @param world The world of the location
     * @return Location or null if the string location is corrupted
     */
    public static Location loadLocation(FileConfiguration config, String path, World world) {
        try {
            double x = config.getDouble(path + ".x");
            double y = config.getDouble(path + ".y");
            double z = config.getDouble(path + ".z");
            float yaw = 0f;
            float pitch = 0f;

            if (config.contains(path + ".yaw"))
                yaw = Float.parseFloat(config.getString(path + ".yaw"));

            if (config.contains(path + ".pitch"))
                pitch = Float.parseFloat(config.getString(path + ".pitch"));

            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Sauvegarder en une chaîne de caractères dans une config une Location
     * @param config The configuration file we want to save as string the Location
     * @param path The path where we want the string Location goes
     */
    public static void saveLocation(FileConfiguration config, String path, Location location) {
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
    }

    public static Vector getBackDirection(Location location) {
        return location.getDirection().normalize().multiply(-0.4);
    }

    public static Vector getFrontDirection(Location location) {
        return location.getDirection().normalize().multiply(0.4);
    }

    public static Vector getRightDirection(Location location) {
        return rotateVectorAroundY(location.getDirection(), 90).normalize();
    }

    public static Vector getLeftDirection(Location location) {
        return rotateVectorAroundY(location.getDirection(), -90).normalize();
    }

    /**
     * Rotates a vector by a given number of degrees; assume looking from a top down view (around the Y axis)
     * @param vector - The vector to rotate
     * @param degrees - The number of degrees to rotate by
     * @return - A rotated vector around the Y axis
     */
    public static Vector rotateVectorAroundY(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);

        double currentX = vector.getX();
        double currentZ = vector.getZ();

        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);

        return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
    }

    public static Location getRandomLocationAroundPlayer(Player player) {

        double newX = player.getLocation().getX() - 1 + new Random().nextInt(2);
        double newZ = player.getLocation().getZ() - 1 + new Random().nextInt(2);
        return new Location(player.getWorld(), newX, player.getLocation().getY(), newZ);

    }

}
