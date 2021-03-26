package fr.maximouz.thepit.listeners;

import fr.euracraft.api.EuraAPI;
import fr.euracraft.api.player.IEuraPlayer;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.displayname.DisplayNameManager;
import fr.maximouz.thepit.events.*;
import fr.maximouz.thepit.prime.Prime;
import fr.maximouz.thepit.prime.PrimeManager;
import fr.maximouz.thepit.inventories.ShopInventory;
import fr.maximouz.thepit.scoreboard.ScoreboardManager;
import fr.maximouz.thepit.statistic.PlayerStatistic;
import fr.maximouz.thepit.inventories.UpgradesInventory;
import fr.maximouz.thepit.statistic.PlayerStatisticsManager;
import fr.maximouz.thepit.tasks.ObsidianBreakTask;
import fr.maximouz.thepit.tasks.PrimeParticleTask;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerListener implements Listener {

    // On Player Joins
    @EventHandler (priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        IEuraPlayer euraPlayer = EuraAPI.getInstance().getEuraPlayer(player.getUniqueId());

        BankManager.getInstance().loadBank(player);
        if (!player.hasPlayedBefore())
            ThePit.getInstance().initPlayer(player);
        else
            player.teleport(ThePit.spawnPoint);
        // load stats
        PlayerStatisticsManager.getInstance().loadPlayerStatistic(player);
        // update kill streak
        PlayerStatisticsManager.getInstance().getPlayerStatistic(player).updateKillStreak();
        // update display name
        ThePit.getInstance().getServer().getOnlinePlayers().forEach(target -> DisplayNameManager.getInstance().update(target));
        // create scoreboard
        euraPlayer.createScoreBoard("thepit");
        ScoreboardManager.getInstance().register(euraPlayer);

        event.setJoinMessage(null);

    }

    // On Player Quit
    @EventHandler (priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Bukkit.getOnlinePlayers().forEach(target -> DisplayNameManager.getInstance().update(target));

        // save bank data
        BankManager.getInstance().saveBank(player);
        // save stats
        PlayerStatisticsManager.getInstance().savePlayerStatistic(player);
        ScoreboardManager.getInstance().remove(player);

        event.setQuitMessage(null);

    }

    // On Player Dies
    @EventHandler (priority = EventPriority.LOW)
    public void onDie(PlayerDeathEvent event) {

        Player player = event.getEntity();

        // Laisser tomber uniquement les armures en fer
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        drops.forEach(item -> {

            if (item.getType() == Material.IRON_SWORD || !item.getType().toString().contains("IRON") || !item.getType().toString().contains("DIAMOND"))
                item.setType(Material.AIR);

        });

        event.setKeepLevel(true);

        Player killer = player.getKiller();

        if (killer != null) {

            Bank killerBank = BankManager.getInstance().getBank(killer);

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

            // Récompenses pour le meurtre
            double exp = ThreadLocalRandom.current().nextDouble(2, 5);
            double gold = ThreadLocalRandom.current().nextDouble(4, 11);

            // Message
            StringBuilder message = new StringBuilder(ChatColor.GREEN + "" + ChatColor.BOLD + "MEURTRE!" + ChatColor.RESET + "" + ChatColor.GRAY + " sur " + ChatColor.RESET + player.getDisplayName());

            EarnXpEvent earnXpEvent = new EarnXpEvent(EarnReason.KILL, killer, killerBank, exp);
            Bukkit.getPluginManager().callEvent(earnXpEvent);

            if (!earnXpEvent.isCancelled()) {
                exp = Format.round(earnXpEvent.getAmount());
                killerBank.setExperience(killerBank.getExperience() + exp);
                message.append(" §b+").append(exp).append("XP");
            }

            EarnGoldEvent earnGoldEvent = new EarnGoldEvent(EarnReason.KILL, killer, killerBank, gold);
            Bukkit.getPluginManager().callEvent(earnGoldEvent);

            if (!earnGoldEvent.isCancelled()) {
                gold = Format.round(earnGoldEvent.getAmount());
                killerBank.setBalance(killerBank.getBalance() + gold);
                message.append(" §6+").append(gold).append("g");
            }

            killer.sendMessage(message.toString());
            player.sendMessage("§c§lMORT! §r§7par§r " + killer.getDisplayName());


            // Ajouter un kill au killer, mettre à jour sa série de meurtres et arrêter son status de combat
            PlayerStatistic killerStatistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(killer);
            killerStatistic.addKill(player.getUniqueId(), System.currentTimeMillis());
            killerStatistic.updateKillStreak();
            killerStatistic.cancelLastDamage();

            // Prévenir d'une éventuelle série de meurtres et ajouter une prime
            if (killerStatistic.getKillStreak() == 10 || killerStatistic.getKillStreak() == 15 || killerStatistic.getKillStreak() % 10 == 0) {

                Bukkit.broadcastMessage("§d§lSERIE! §7de §d" + killerStatistic.getKillStreak() + " meurtres §7par §r" + killer.getDisplayName());
                Prime killerPrime = PrimeManager.getInstance().getPrime(killer);

                if (killerPrime == null) {

                    killerPrime = new Prime(killer, 50.0);
                    PrimeStartEvent primeStartEvent = new PrimeStartEvent(killer, killerPrime);
                    Bukkit.getPluginManager().callEvent(primeStartEvent);

                    if (!primeStartEvent.isCancelled()) {

                        PrimeManager.getInstance().createPrime(killerPrime);
                        Bukkit.broadcastMessage("§6§lPRIME! §7de §650.0g §7sur§r " + killer.getDisplayName() + " §7pour sa série de meurtres élevée");
                        new PrimeParticleTask(player, killerPrime).runTaskTimer(ThePit.getInstance(), 0L, 1L);

                    }
                } else {

                    double bump = killerStatistic.getKillStreak() <= 10 ? 150.0 : 250.0;
                    killerPrime.bumpGold(bump);
                    Bukkit.broadcastMessage("§6§lPRIME! §7augmentation de §6" + bump + "g §7sur§r " + killer.getDisplayName() + " §7pour sa série de meurtres élevée");

                }
            }

            DisplayNameManager.getInstance().update(killer);
            killerBank.updateExpBar();

            // Donner une pomme dorée (si l'event n'a pas été cancel)
            PlayerReceiveGAppleEvent playerReceiveGAppleEvent = new PlayerReceiveGAppleEvent(killer);
            Bukkit.getPluginManager().callEvent(playerReceiveGAppleEvent);

            if (!playerReceiveGAppleEvent.isCancelled()) {

                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));

            }


        } else {

            player.sendMessage("§c§lMORT! §r§7Vous êtes mort d'une raison inconnue");

        }

        // Ajouter une mort au joueur qui vient de mourir, mettre à jour sa série de meurtres, arrêter son status de combat
        PlayerStatistic playerStatistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(player);
        playerStatistic.addDeath(killer != null ? killer.getUniqueId() : null, System.currentTimeMillis());
        playerStatistic.updateKillStreak();
        playerStatistic.cancelLastDamage();

        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
            player.spigot().respawn();
            DisplayNameManager.getInstance().update(player);
        }, 2L);
        event.setDeathMessage(null);
    }

    // On Player respawn
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        ThePit.getInstance().initPlayer(event.getPlayer());
    }
    // On Player Drops Item
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();

        if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE) {

            Item item = event.getItemDrop();
            ItemStack itemStack = item.getItemStack();

            if (itemStack.getType() != Material.ARROW && itemStack.getType() != Material.DIAMOND_SWORD && !(itemStack.getType().toString().contains("HELMET") || itemStack.getType().toString().contains("CHESTPLATE") || itemStack.getType().toString().contains("LEGGINGS") || itemStack.getType().toString().contains("BOOTS")) && !(itemStack.getType().toString().contains("IRON") || itemStack.getType().toString().contains("DIAMOND"))) {

                 event.setCancelled(true);

            }

        }


    }

    // On Player Pickup Item
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE) {

            event.setCancelled(true);

            ItemStack item = event.getItem().getItemStack();

            // Remplacer l'armure en maille par l'armure en fer ramassé
            if (item.getType() == Material.IRON_CHESTPLATE && (player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE)) {

                player.getInventory().setChestplate(item);
                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);
                event.getItem().remove();

            } else if (item.getType() == Material.IRON_LEGGINGS && (player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType() == Material.CHAINMAIL_LEGGINGS)) {

                player.getInventory().setLeggings(item);
                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);
                event.getItem().remove();

            } else if (item.getType() == Material.IRON_BOOTS && (player.getInventory().getBoots() == null || player.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS)) {

                player.getInventory().setBoots(item);
                player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);
                event.getItem().remove();

            } else if (item.getType() == Material.GOLD_INGOT) {

                Bank bank = BankManager.getInstance().getBank(player);
                double amount = ThreadLocalRandom.current().nextInt(3, 9);

                EarnGoldEvent earnGoldEvent = new EarnGoldEvent(EarnReason.PICK_UP, player, bank, amount);
                Bukkit.getPluginManager().callEvent(earnGoldEvent);

                if (!earnGoldEvent.isCancelled()) {

                    amount = Format.round(earnGoldEvent.getAmount());

                    bank.pay(amount);
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
                    player.sendMessage("§6§lGOLD! §r§7Ramassage au sol de §6" + amount + "g");
                    event.getItem().remove();

                } else {

                    event.setCancelled(true);

                }

            } else {

                if (item.getType() != Material.IRON_HELMET && item.getType() != Material.IRON_CHESTPLATE && item.getType() != Material.IRON_LEGGINGS && item.getType() != Material.IRON_BOOTS) {

                    event.setCancelled(false);
                    return;

                }

                ItemStack[] contents = player.getInventory().getContents();

                for (int i = 9; i < 36; i++) {

                    if (contents[i] == null) {

                        event.getItem().remove();
                        player.getInventory().setItem(i, item);
                        return;

                    }

                }

                player.getInventory().addItem(item);

            }

        }


    }

    // On Damage
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER || event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        PlayerStatisticsManager.getInstance().getPlayerStatistic(damager).damage();
        PlayerStatisticsManager.getInstance().getPlayerStatistic(damaged).damage();

    }

    // On Player Talks
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        event.setFormat("%s" + ChatColor.GRAY + ": %s");

        Bank bank = BankManager.getInstance().getBank(event.getPlayer());
        if (event.getMessage().equalsIgnoreCase("shop")) {
            new ShopInventory(event.getPlayer(), bank).open();
        } else if (event.getMessage().equalsIgnoreCase("upgrades")) {
            new UpgradesInventory(event.getPlayer(), bank, ThePit.getInstance()).open();
        }

    }

    // On Block Break
    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }

        if (event.getBlock().getType() == Material.OBSIDIAN) {
            ObsidianBreakTask task = ObsidianBreakTask.getTask(event.getBlock().getLocation());
            if (task != null)
                task.cancel();
        }

    }

    // On Player Places Block
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        if (!player.isOp() || player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }

    }

    // On Entity Explodes
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        event.setYield(0f);

    }

    // On Block Explodes
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {

        if (event.getBlock().getType() != Material.TNT)
            return;

        ((TNTPrimed) event.getBlock()).setCustomName("§e§lTu ne casseras pas la map :(");
        ((TNTPrimed) event.getBlock()).setCustomNameVisible(true);
        ((TNTPrimed) event.getBlock()).remove();

    }

    // On Player Fall Damage
    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {

        if (event.getEntity().getType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {

            event.setCancelled(true);

        }

    }

    // On Food Level Changes
    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {

        event.setCancelled(true);

    }

}
