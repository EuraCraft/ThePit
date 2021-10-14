package fr.maximouz.thepit.commands;

import fr.euracraft.api.command.AbstractCommand;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnPointCommand extends AbstractCommand {

    public SpawnPointCommand() {
        super(ThePit.getInstance(), "spawnpoint.use");
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 1) {

                if (args[0].equalsIgnoreCase("help")) {

                    player.sendMessage("§e/" + label + " add");
                    player.sendMessage("§e/" + label + " list");
                    player.sendMessage("§e/" + label + " remove <index>");
                    player.sendMessage("§e/" + label + " clear");
                    player.sendMessage("§e/" + label + " help");

                } else if (args[0].equalsIgnoreCase("add")) {

                    Location location = player.getLocation();
                    ThePit.getInstance().getSpawnPoints().add(location);
                    player.sendMessage("§aPoint d'appartition ajouté.");

                } else if (args[0].equalsIgnoreCase("list")) {

                    int size = ThePit.getInstance().getSpawnPoints().size();

                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            Location spawnPoint = ThePit.getInstance().getSpawnPoints().get(i);
                            player.sendMessage("§6" + i + " - §e" + WorldUtils.toString(spawnPoint).replace(",", " "));
                        }
                    } else {
                        player.sendMessage("§cAucun point d'apparition trouvé..");
                    }

                } else if (args[0].equalsIgnoreCase("clear")) {

                    int size = ThePit.getInstance().getSpawnPoints().size();
                    ThePit.getInstance().getSpawnPoints().clear();

                    player.sendMessage("§b" + size + "§e points d'apparition ont bien été supprimés.");

                }

            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {

                try {

                    int index = Integer.parseInt(args[1]);
                    int size = ThePit.getInstance().getSpawnPoints().size();

                    if (index < size) {

                        ThePit.getInstance().getSpawnPoints().remove(index);
                        player.sendMessage("§ePoint d'apparition à l'index §6" + index + "§e supprimé.");

                    } else {

                        player.sendMessage("§cAucun point d'apparition n'est à cet index");

                    }

                } catch (NumberFormatException ex) {

                    player.sendMessage("§e" + args[1] + "§c n'est pas un nombre valide..");

                }

            } else {

                sender.sendMessage("§c/" + label + " help");

            }

        } else {

            sender.sendMessage("Commande utilisable en tant que joueur..");

        }

        return false;
    }

}
