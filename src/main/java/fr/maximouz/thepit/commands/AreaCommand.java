package fr.maximouz.thepit.commands;

import fr.euracraft.api.command.AbstractCommand;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.area.Area;
import fr.maximouz.thepit.area.AreaManager;
import fr.maximouz.thepit.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class AreaCommand extends AbstractCommand implements Listener {

    private static final AreaCommand INSTANCE = new AreaCommand();

    private final Map<Player, Location> pos1;
    private final Map<Player, Location> pos2;

    public AreaCommand() {
        super(ThePit.getInstance(), "area.use");
        pos1 = new HashMap<>();
        pos2 = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance());
    }

    public static AreaCommand getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("help")) {

                    player.sendMessage("§e/" + label + " pos1/pos2");
                    player.sendMessage("§e/" + label + " create <name> <display name>");
                    player.sendMessage("§e/" + label + " remove <name>");
                    player.sendMessage("§e/" + label + " list");
                    player.sendMessage("§e/" + label + " help");

                } else if (args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2")) {

                    Location location = player.getLocation();
                    StringBuilder message = new StringBuilder("§aPosition §b");
                    if (args[0].equalsIgnoreCase("pos1")) {
                        pos1.put(player, location);
                        message.append("1");
                    } else {
                        pos2.put(player, location);
                        message.append("2");
                    }
                    message.append(" §adéfinie.");
                    player.sendMessage(message.toString());

                } else if (args[0].equalsIgnoreCase("list")) {

                    AreaManager.getInstance().getAreas().forEach(area -> player.sendMessage("§f" + area.getName() + " §7§m-*-§r " + area.getDisplayName()));

                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {

                String areaName = args[1];
                Area area = AreaManager.getInstance().getArea(areaName);

                if (area != null) {
                    AreaManager.getInstance().getAreas().remove(area);
                    player.sendMessage("§eZone §7" + area.getName() + "§e supprimée.");
                } else {
                    player.sendMessage("§cZone introuvable..");
                }

            } else if (args.length >= 3 && args[0].equalsIgnoreCase("create")) {

                String areaName = args[1];
                Area area = AreaManager.getInstance().getArea(areaName);

                if (area == null) {

                    Location l1 = pos1.get(player);
                    Location l2 = pos2.get(player);

                    if (l1 == null) {
                        player.sendMessage("§cVous n'avez pas défini la §epos1§c..");
                    } else if (l2 == null) {
                        player.sendMessage("§cVous n'avez pas défini la §epos2§c..");
                    } else {

                        StringBuilder displayName = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            displayName.append(args[i]).append(i + 1 < args.length ? " " : "");
                        }
                        area = new Area(new Cuboid(l1, l2), areaName, displayName.toString());
                        AreaManager.getInstance().getAreas().add(area);
                        player.sendMessage("§aZone §f" + areaName + "§a créée.");
                        pos1.remove(player);
                        pos2.remove(player);

                    }

                } else {

                    player.sendMessage("§cUne zone porte déjà ce nom..");

                }
            }

        } else {

            sender.sendMessage("Commande utilisable en tant que joueur");

        }

        return false;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        pos1.remove(event.getPlayer());
        pos2.remove(event.getPlayer());

    }

}
