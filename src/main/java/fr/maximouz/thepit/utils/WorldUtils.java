package fr.maximouz.thepit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.regex.Pattern;

public class WorldUtils {

    /**
     * Obtenir une Location à partir d'une chaîne de caractère
     * @param location location in String
     * @return Location or null if the string location is corrupted
     */
    public static Location fromString(String location) {
        String[] args = location.split(Pattern.quote(","));
        String worldName = args[0];
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            try {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                float yaw = Float.parseFloat(args[4]);
                float pitch = Float.parseFloat(args[5]);

                return new Location(world, x, y, z, yaw, pitch);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return new Location(Bukkit.getWorld("world"), 0,0,0);
    }

    /**
     * Sauvegarder en une chaîne de caractères dans une config une Location
     * @param location The location we want to turn into String
     */
    public static String toString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + ","  + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    public static Vector getBackDirection(Location location) {
        return location.getDirection().normalize().multiply(-1.4);
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
