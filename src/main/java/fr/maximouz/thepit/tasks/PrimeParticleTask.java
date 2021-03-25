package fr.maximouz.thepit.tasks;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.prime.Prime;
import fr.maximouz.thepit.prime.PrimeManager;
import fr.maximouz.thepit.utils.Reflection;
import fr.maximouz.thepit.utils.WorldUtils;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PrimeParticleTask extends BukkitRunnable {

    private final Player player;
    private final Prime prime;

    public PrimeParticleTask(Player player, Prime prime) {
        this.player = player;
        this.prime = prime;
    }


    @Override
    public void run() {

        Bukkit.getOnlinePlayers().forEach(target -> {

            if (target != player) {

                if (!PrimeManager.getInstance().getPrimes().contains(prime)) {
                    cancel();
                    return;
                }

                spawn(target, WorldUtils.getRandomLocationAroundPlayer(player));

            }

        });

    }

    private void spawn(Player target, Location location) {

        Object worldServer = Reflection.getHandle(Reflection.getOBCClass("CraftWorld").cast(player.getWorld()));
        try {

            Object entityArmorStand = Reflection.callConstructor(Reflection.getNMSClass("EntityArmorStand").getConstructor(Reflection.getNMSClass("World")), worldServer);

            Class<?> entityClass = Reflection.getNMSClass("Entity");
            Object entity = entityClass.cast(entityArmorStand);

            Class<?> entityLivingClass = Reflection.getNMSClass("EntityLiving");
            // setLocation
            Reflection.callMethod(Reflection.getMethod(entityClass, "setLocation", double.class, double.class, double.class, float.class, float.class), entityArmorStand, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            // setCustomName
            Reflection.callMethod(Reflection.getMethod(entityClass, "setCustomName", String.class), entityArmorStand, "§6§l" + prime.getGold() + "g");
            // setCustomNameVisible
            Reflection.callMethod(Reflection.getMethod(entityClass, "setCustomNameVisible", boolean.class), entityArmorStand, true);
            // setGravity
            Reflection.callMethod(Reflection.getMethod(entityArmorStand.getClass(), "setGravity", boolean.class), entityArmorStand, true);
            // setSmall
            Reflection.callMethod(Reflection.getMethod(entityArmorStand.getClass(), "setSmall", boolean.class), entityArmorStand, true);
            // setInvisible
            Reflection.callMethod(Reflection.getMethod(entityArmorStand.getClass(), "setInvisible", boolean.class), entityArmorStand, true);

            Object spawnPacket = Reflection.callConstructor(Reflection.getNMSClass("PacketPlayOutSpawnEntityLiving").getConstructor(entityLivingClass), entityArmorStand);
            Reflection.sendPacket(target, spawnPacket);

            int id = Reflection.callMethod(Reflection.getMethod(entityArmorStand.getClass(), "getId"), entityArmorStand);

            Object teleportPacket = Reflection.callConstructor(Reflection.getConstructor(Reflection.getNMSClass("PacketPlayOutEntityTeleport"), entityClass), entity);
            Reflection.setField(Reflection.getField(teleportPacket.getClass(), true, "c"), teleportPacket, MathHelper.floor((location.getY() + 1) * 32.0D));
            Reflection.sendPacket(target, teleportPacket);

            Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {

                try {

                    Object destroyPacket = Reflection.callConstructor(Reflection.getConstructor(Reflection.getNMSClass("PacketPlayOutEntityDestroy"), int[].class), new int[] { id });
                    Reflection.sendPacket(target, destroyPacket);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }, 4L);

        } catch (NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}
