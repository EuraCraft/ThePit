package fr.maximouz.thepit;

import fr.euracraft.api.EuraAPI;
import fr.euracraft.api.item.ItemBuilder;
import fr.euracraft.api.player.IEuraPlayer;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.events.PlayerInitEvent;
import fr.maximouz.thepit.listeners.PlayerListener;
import fr.maximouz.thepit.prime.PrimeManager;
import fr.maximouz.thepit.scoreboard.ScoreboardManager;
import fr.maximouz.thepit.statistic.PlayerStatistic;
import fr.maximouz.thepit.statistic.PlayerStatisticsManager;
import fr.maximouz.thepit.tasks.GoldSpawnTask;
import fr.maximouz.thepit.upgrade.UpgradeManager;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.utils.Cuboid;
import fr.maximouz.thepit.utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ThePit extends JavaPlugin {

    private static ThePit INSTANCE;

    // Constantes
    public static Location spawnPoint;

    // areas
    public static Cuboid mapArea;
    public static Cuboid centerArea;
    public static Map<String, Cuboid> areas;

    @Override
    public void onEnable() {

        INSTANCE = this;

        // Load Constantes from configuration file

        saveDefaultConfig();

        EuraAPI.getInstance().loadTranslation(this);

        spawnPoint = WorldUtils.loadLocation(getConfig(), "spawn");

        World mapAreaWorld = getServer().getWorld(getConfig().getString("map_area.world"));

        mapArea = new Cuboid(WorldUtils.loadLocation(getConfig(), "map_area.first_position", mapAreaWorld), WorldUtils.loadLocation(getConfig(), "map_area.second_position", mapAreaWorld));
        mapArea = new Cuboid(WorldUtils.loadLocation(getConfig(), "center_area.first_position", mapAreaWorld), WorldUtils.loadLocation(getConfig(), "center_area.second_position", mapAreaWorld));

        areas = new HashMap<>();
        getConfig().getConfigurationSection("areas").getKeys(false).forEach(areaName -> areas.put(areaName, new Cuboid(WorldUtils.loadLocation(getConfig(), "areas." + areaName + ".first_position", mapAreaWorld), WorldUtils.loadLocation(getConfig(), "areas." + areaName + ".second_position", mapAreaWorld))));

        PerkManager.getInstance().getPerks().forEach(Perk::loadAll);

        registerListeners();

        getServer().getOnlinePlayers().forEach(player -> {
            // load bank data
            BankManager.getInstance().loadBank(player);
            // load stats
            PlayerStatisticsManager.getInstance().getPlayerStatistics().add(new PlayerStatistic(player));
            // load prime
            PrimeManager.getInstance().loadPrime(player);
            // scoreboard
            IEuraPlayer euraPlayer = EuraAPI.getInstance().getEuraPlayer(player.getUniqueId());
            euraPlayer.createScoreBoard("thepit");
            ScoreboardManager.getInstance().register(euraPlayer);
        });

        GoldSpawnTask.getInstance().runTaskTimer(this, 0, 20);

    }

    @Override
    public void onDisable() {

        getServer().getOnlinePlayers().forEach(player -> {
            BankManager.getInstance().saveBank(player);
            PrimeManager.getInstance().savePrime(player);
            PlayerStatisticsManager.getInstance().savePlayerStatistic(player);
            UpgradeManager.getInstance().getUpgrades().forEach(upgrade -> upgrade.save(player));

            IEuraPlayer euraPlayer = EuraAPI.getInstance().getEuraPlayer(player.getUniqueId());
            ScoreboardManager.getInstance().register(euraPlayer);
        });

        ScoreboardManager.getInstance().disable();

        if (GoldSpawnTask.getInstance().hasStarted())
            GoldSpawnTask.getInstance().cancel();

    }

    public static ThePit getInstance() {
        return INSTANCE;
    }

    /**
     * Instancier les listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        UpgradeManager.getInstance().registerListeners();
        PerkManager.getInstance().registerListeners();
    }

    /**
     * Initialiser un joueur au ThePurge (téléportation, stuff..)
     * @param player Concerned player
     */
    public void initPlayer(Player player) {

        // Partie de l'armure en fer
        Random random = new Random();
        int ironItem = random.nextInt(3);

        ItemStack chestPlate = new ItemBuilder(ironItem == 0 ? Material.IRON_CHESTPLATE : Material.CHAINMAIL_CHESTPLATE).setUnbreakable(true).build();
        ItemStack leggings = new ItemBuilder(ironItem == 1 ? Material.IRON_LEGGINGS : Material.CHAINMAIL_LEGGINGS).setUnbreakable(true).build();
        ItemStack boots = new ItemBuilder(ironItem == 2 ? Material.IRON_BOOTS : Material.CHAINMAIL_BOOTS).setUnbreakable(true).build();
        ItemStack sword = new ItemBuilder(Material.IRON_SWORD).setUnbreakable(true).build();
        ItemStack[] others = new ItemStack[] {
                new ItemBuilder(Material.BOW).setUnbreakable(true).build(),
                null, null, null, null, null, null,
                new ItemBuilder(Material.ARROW, 32).build()
        };

        PlayerInitEvent event = new PlayerInitEvent(player, sword, null, chestPlate, leggings, boots, others);
        getServer().getPluginManager().callEvent(event);

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));

        player.getInventory().setHelmet(event.getHelmet());
        player.getInventory().setChestplate(event.getChestPlate());
        player.getInventory().setLeggings(event.getLeggings());
        player.getInventory().setBoots(event.getBoots());

        player.getInventory().setItem(0, event.getSword());
        for (ItemStack item : event.getOthers()) {
            int firstEmpty = player.getInventory().firstEmpty();
            if (firstEmpty == -1)
                break;
            player.getInventory().setItem(firstEmpty, item);
        }

    }

}
