package fr.maximouz.thepit;

import fr.euracraft.api.EuraAPI;
import fr.euracraft.api.item.ItemBuilder;
import fr.euracraft.api.player.IEuraPlayer;
import fr.maximouz.thepit.area.AreaManager;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.bank.Prestige;
import fr.maximouz.thepit.commands.AreaCommand;
import fr.maximouz.thepit.commands.SpawnPointCommand;
import fr.maximouz.thepit.displayname.DisplayNameManager;
import fr.maximouz.thepit.events.*;
import fr.maximouz.thepit.inventories.ShopInventory;
import fr.maximouz.thepit.inventories.prestige.PrestigeInventory;
import fr.maximouz.thepit.inventories.quest.QuestInventory;
import fr.maximouz.thepit.inventories.upgrade.UpgradesInventory;
import fr.maximouz.thepit.listeners.DropItemListener;
import fr.maximouz.thepit.listeners.PickupListener;
import fr.maximouz.thepit.listeners.PlayerListener;
import fr.maximouz.thepit.prime.Prime;
import fr.maximouz.thepit.prime.PrimeManager;
import fr.maximouz.thepit.quest.QuestManager;
import fr.maximouz.thepit.scoreboard.ScoreboardManager;
import fr.maximouz.thepit.statistic.PlayerStatistic;
import fr.maximouz.thepit.statistic.PlayerStatisticsManager;
import fr.maximouz.thepit.tasks.GoldSpawnTask;
import fr.maximouz.thepit.tasks.PrimeParticleTask;
import fr.maximouz.thepit.upgrade.UpgradeManager;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.utils.Configuration;
import fr.maximouz.thepit.utils.WorldUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ThePit extends JavaPlugin {

    private static ThePit INSTANCE;

    private List<Location> spawnPoints;

    private Configuration npcConfiguration;

    private QuestManager questManager;

    @Override
    public void onEnable() {

        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("§cRedémarrage :)"));

        getLogger().info("\n\n _______  _            _____  _  _   \n" +
                "|__   __|| |          |  __ \\(_)| |  \n" +
                "   | |   | |__    ___ | |__) |_ | |_ \n" +
                "   | |   | '_ \\  / _ \\|  ___/| || __|\n" +
                "   | |   | | | ||  __/| |    | || |_ \n" +
                "   |_|   |_| |_| \\___||_|    |_| \\__|\n\n");

        INSTANCE = this;

        saveDefaultConfig();
        spawnPoints = new ArrayList<>();
        loadSpawnPoints();

        npcConfiguration = new Configuration(this, "npc");
        npcConfiguration.saveDefaultConfig();

        AreaManager.getInstance().loadAreas();
        EuraAPI.getInstance().loadTranslation(this);

        registerManagers();
        registerListeners();
        registerCommands();

        GoldSpawnTask.getInstance().runTaskTimerAsynchronously(this, 0, 20);

        setupNPCs();

    }

    @Override
    public void onDisable() {

        getServer().getOnlinePlayers().forEach(player -> {
            BankManager.getInstance().saveBank(player);
            PlayerStatisticsManager.getInstance().savePlayerStatistic(player);
            UpgradeManager.getInstance().getUpgrades().forEach(upgrade -> upgrade.save(player));

            IEuraPlayer euraPlayer = EuraAPI.getInstance().getEuraPlayer(player.getUniqueId());
            ScoreboardManager.getInstance().register(euraPlayer);
        });

        ScoreboardManager.getInstance().disable();

        if (GoldSpawnTask.getInstance().hasStarted())
            GoldSpawnTask.getInstance().cancel();

        saveSpawnPoints();
        saveConfig();

    }

    public static ThePit getInstance() {
        return INSTANCE;
    }

    /**
     * Charger les points d'apparition
     */
    private void loadSpawnPoints() {
        getConfig().getStringList("spawn_points").forEach(location -> spawnPoints.add(WorldUtils.fromString(location)));
    }

    /**
     * Charger les points d'apparition
     */
    private void saveSpawnPoints() {
        List<String> spawnPoints = new ArrayList<>();
        this.spawnPoints.forEach(spawnPoint -> spawnPoints.add(WorldUtils.toString(spawnPoint)));
        getConfig().set("spawn_points", spawnPoints);
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    /**
     * Point d'apparition aléatoire
     * @return random Location
     */
    public Location getRandomSpawnPoint() {
        return getSpawnPoints().get(ThreadLocalRandom.current().nextInt(getSpawnPoints().size()));
    }

    /**
     * Instancier les managers
     */
    public void registerManagers() {
        questManager = new QuestManager();
    }

    /**
     * Enregistrer les listeners
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new DropItemListener(), this);
        getServer().getPluginManager().registerEvents(new PickupListener(), this);
        UpgradeManager.getInstance().registerListeners();
        PerkManager.getInstance().registerListeners();
    }

    /**
     * Enregistrer les commandes
     */
    private void registerCommands() {
        getCommand("area").setExecutor(new AreaCommand());
        getCommand("spawnpoint").setExecutor(new SpawnPointCommand());
    }

    /**
     * Tuer un joueur
     * @param player Player mort
     * @param drops Items qui vont tomber au sol
     */
    private static void kill(Player player, List<ItemStack> drops) {

        Location deathLocation = player.getLocation();

        player.getInventory().clear();
        INSTANCE.initPlayer(player);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);

        for (ItemStack item : drops) {

            if (item != null && item.getType() != Material.AIR) {

                deathLocation.getWorld().dropItem(deathLocation, item);

            }

        }

        // Assistances
        double damageTaken = PlayerStatisticsManager.getInstance().getDamageTaken(player);
        for (PlayerStatistic statistic : PlayerStatisticsManager.getInstance().getPlayerStatistics()) {

            Player assistPlayer = statistic.getPlayer();

            // Si ce n'est pas lui même ou son tueur
            if (player == assistPlayer && (player.getKiller() != null && player.getKiller() != assistPlayer))
                continue;

            double damage = statistic.getDamage(player);

            if (damage > 0) {

                BigDecimal exp = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(2.0, 3.1)).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal gold = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(2.0, 2.1)).setScale(2, RoundingMode.HALF_EVEN);

                PlayerAssistEvent playerAssistEvent = new PlayerAssistEvent(statistic.getPlayer(), player, exp, gold);
                Bukkit.getPluginManager().callEvent(playerAssistEvent);

                if (playerAssistEvent.isCancelled())
                    continue;

                statistic.setAssists(statistic.getAssists() + 1);
                Bank assistBank = BankManager.getInstance().getBank(assistPlayer.getUniqueId());

                String percent = (int) (damage / damageTaken * 100) + "%";
                StringBuilder message = new StringBuilder("§a§lASSIST!§r§7 " + percent + " sur §r" + player.getDisplayName());

                if (playerAssistEvent.getExperienceReward().compareTo(BigDecimal.ZERO) > 0) {
                    assistBank.addExperience(playerAssistEvent.getExperienceReward());
                    message.append("§b +").append(playerAssistEvent.getExperienceReward().toString()).append("XP");
                }

                if (playerAssistEvent.getGoldReward().compareTo(BigDecimal.ZERO) > 0) {
                    assistBank.setBalance(assistBank.getBalance().add(playerAssistEvent.getGoldReward()));
                    message.append("§6 +").append(playerAssistEvent.getGoldReward().toString()).append("g");
                }

                assistBank.updateExpBar(assistPlayer);
                assistPlayer.sendMessage(message.toString());
                assistPlayer.playSound(assistPlayer.getLocation(), Sound.NOTE_SNARE_DRUM, 1f, 1f);

                DisplayNameManager.getInstance().update(assistPlayer);

            }
        }

        PlayerStatisticsManager.getInstance().removeAssistsDamage(player);

        PlayerStatistic statistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(player);
        // reset killstreak
        KillStreakUpdateEvent killStreakUpdateEvent = new KillStreakUpdateEvent(statistic, 0);
        Bukkit.getPluginManager().callEvent(killStreakUpdateEvent);

        if (!killStreakUpdateEvent.isCancelled())
            statistic.setKillStreak(killStreakUpdateEvent.getNewKillStreak());

    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    /**
     * Tuer un joueur
     * @param player Victime
     * @param killer Tueur
     */
    public static void kill(Player player, Player killer) {

        if (player != killer) {

            PlayerStatistic killerStatistic = null;
            Bank killerBank = null;

            if (killer != null) {

                // Ajouter un kill au tueur et mettre à jour sa série de meurtres
                killerStatistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(killer);
                killerBank = BankManager.getInstance().getBank(killer);

                // killstreak
                KillStreakUpdateEvent killStreakUpdateEvent = new KillStreakUpdateEvent(killerStatistic, killerStatistic.getKillStreak() + 1);
                Bukkit.getPluginManager().callEvent(killStreakUpdateEvent);

                if (!killStreakUpdateEvent.isCancelled())
                    killerStatistic.setKillStreak(killStreakUpdateEvent.getNewKillStreak());

            }

            // Récompenses pour le meurtre
            BigDecimal exp = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(2.0, 5.0)).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal gold = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(4.0, 11.0)).setScale(2, RoundingMode.HALF_EVEN);

            List<ItemStack> drops = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
            drops.addAll(Arrays.asList(player.getInventory().getArmorContents()));

            PlayerKillEvent playerKillEvent = new PlayerKillEvent(killer, player, exp, gold, drops);
            Bukkit.getPluginManager().callEvent(playerKillEvent);

            playerKillEvent.removeMaterialFromDrop(Material.GOLDEN_APPLE);

            kill(player, playerKillEvent.getDrops());

            if (killer != null) {

                // Message pour le tueur
                StringBuilder message = new StringBuilder("§a§lMEURTRE!§r§7 sur §r" + player.getDisplayName());

                if (playerKillEvent.getExperienceReward().compareTo(BigDecimal.ZERO) > 0) {
                    killerBank.addExperience(playerKillEvent.getExperienceReward());
                    message.append(" §b+").append(exp.toString()).append("XP");
                }

                if (playerKillEvent.getGoldReward().compareTo(BigDecimal.ZERO) > 0) {
                    killerBank.setBalance(killerBank.getBalance().add(playerKillEvent.getGoldReward()));
                    message.append(" §6+").append(gold.toString()).append("g");
                }

                killer.sendMessage(message.toString());
                killerStatistic.setKills(killerStatistic.getKills() + 1);

                // Prime de la victime
                Prime victimPrime = PrimeManager.getInstance().getPrime(player);
                if (victimPrime != null) {

                    PrimeEndEvent primeEndEvent = new PrimeEndEvent(player, victimPrime);
                    Bukkit.getPluginManager().callEvent(primeEndEvent);

                    if (!primeEndEvent.isCancelled()) {

                        PrimeManager.getInstance().deletePrime(victimPrime);
                        killerBank.pay(victimPrime.getGold());
                        Bukkit.broadcastMessage("§6§lPRIME OBTENUE! §r" + killer.getDisplayName() + "§7 a tué §r" + player.getDisplayName() + "§7 pour §6" + victimPrime.getGold() + "g");

                    }

                }

                DisplayNameManager.getInstance().update(killer);
                killerBank.updateExpBar(killer);

                // Donner une pomme dorée
                ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1);
                PlayerReceiveGAppleEvent playerReceiveGAppleEvent = new PlayerReceiveGAppleEvent(killer, item);
                Bukkit.getPluginManager().callEvent(playerReceiveGAppleEvent);

                if (!playerReceiveGAppleEvent.isCancelled())
                    killer.getInventory().addItem(playerReceiveGAppleEvent.getItem());

                // Prévenir d'une éventuelle série de meurtres et ajouter une prime
                if (killerStatistic.getKillStreak() == 5 || killerStatistic.getKillStreak() == 10 || killerStatistic.getKillStreak() == 15 || killerStatistic.getKillStreak() % 10 == 0) {
                    Bukkit.broadcastMessage("§d§lSERIE! §7de §d" + killerStatistic.getKillStreak() + " meurtres §7par §r" + killer.getDisplayName());
                    Prime killerPrime = PrimeManager.getInstance().getPrime(killer);

                    if (killerPrime == null) {

                        killerPrime = new Prime(killer, new BigDecimal("50.0"));
                        PrimeStartEvent primeStartEvent = new PrimeStartEvent(killer, killerPrime);
                        Bukkit.getPluginManager().callEvent(primeStartEvent);

                        if (!primeStartEvent.isCancelled()) {
                            PrimeManager.getInstance().createPrime(killerPrime);
                            Bukkit.broadcastMessage("§6§lPRIME! §7de §650.0g §7sur§r " + killer.getDisplayName() + " §7pour sa série de meurtres élevée");
                            new PrimeParticleTask(killer, killerPrime).runTaskTimer(ThePit.getInstance(), 0L, 5L);
                        }

                    } else {

                        BigDecimal bump = BigDecimal.valueOf(killerStatistic.getKillStreak() <= 10 ? 100.0 : 200.0).setScale(2, RoundingMode.HALF_EVEN);
                        killerPrime.bumpGold(bump);
                        Bukkit.broadcastMessage("§6§lPRIME! §7augmentation de §6" + bump + "g §7sur§r " + killer.getDisplayName() + " §7pour sa série de meurtres élevée");

                    }
                }

            }

            // Message pour la victime
            player.sendMessage("§c§lMORT! §r§7" + (killer != null ? "par§r " + killer.getDisplayName() + "" : "d'une raison inconnue"));

        } else {

            List<ItemStack> drops = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
            drops.addAll(Arrays.asList(player.getInventory().getArmorContents()));

            PlayerKillEvent playerKillEvent = new PlayerKillEvent(killer, player, BigDecimal.ZERO, BigDecimal.ZERO, drops);
            Bukkit.getPluginManager().callEvent(playerKillEvent);

            playerKillEvent.removeMaterialFromDrop(Material.GOLDEN_APPLE);

            kill(player, playerKillEvent.getDrops());

            // Message pour la victime
            player.sendMessage("§c§lSUICIDE! §7pourquoi donc ? :(");

        }

    }

    /**
     * Initialiser un joueur au ThePurge (téléportation, stuff..)
     * @param player Concerned player
     */
    public void initPlayer(Player player) {

        // Partie de l'armure qui sera en fer
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

        player.teleport(getRandomSpawnPoint());

    }

    private void setupNPCs() {

        Location location = WorldUtils.fromString(npcConfiguration.getConfig().getString("shop"));
        EuraAPI.getInstance().getNPCManager().createVillager(location, 3, "§6§lITEMS", "§7Temporaire")
                .setCallback(((rightClick, player) -> {
                    new ShopInventory(player, BankManager.getInstance().getBank(player)).open();
                    player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f);
                }));

        location = WorldUtils.fromString(npcConfiguration.getConfig().getString("upgrades"));
        EuraAPI.getInstance().getNPCManager().createVillager(location, 1, "§a§lAMELIORATIONS", "§7Permanent")
                .setCallback((rightClick, player) -> {
                    new UpgradesInventory(player, BankManager.getInstance().getBank(player)).open();
                    player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f);
                });

        location = WorldUtils.fromString(npcConfiguration.getConfig().getString("quests"));
        EuraAPI.getInstance().getNPCManager().createVillager(location, 1, "§3§lQUETES", "§7Quêtes et contrats")
                .setCallback((rightClick, player) -> {
                    new QuestInventory(player).open();
                    player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f);
                });

        location = WorldUtils.fromString(npcConfiguration.getConfig().getString("prestige"));
        EuraAPI.getInstance().getNPCManager().createVillager(location, 1, "§e§lPRESTIGE", "§7Resets & Renomée")
                .setCallback((rightClick, player) -> {
                    Bank bank = BankManager.getInstance().getBank(player);
                    if (bank.getPrestige() != Prestige.ZERO || bank.getLevel() == Level.ONE_HUNDRED_AND_TWENTY) {
                        new PrestigeInventory(player).open();
                        player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f);
                    } else {
                        player.sendMessage("§cReviens me voir quand tu es niveau 120 !");
                    }
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                });


    }

}
