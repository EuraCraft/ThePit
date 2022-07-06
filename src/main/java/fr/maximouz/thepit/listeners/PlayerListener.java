package fr.maximouz.thepit.listeners;

import fr.euracraft.api.EuraAPI;
import fr.euracraft.api.player.IEuraPlayer;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.area.Area;
import fr.maximouz.thepit.area.AreaManager;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.displayname.DisplayNameManager;
import fr.maximouz.thepit.events.*;
import fr.maximouz.thepit.scoreboard.ScoreboardManager;
import fr.maximouz.thepit.statistic.PlayerStatistic;
import fr.maximouz.thepit.statistic.PlayerStatisticsManager;
import fr.maximouz.thepit.utils.Title;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class PlayerListener implements Listener {

    // PlayerJoinEvent
    @EventHandler (priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        IEuraPlayer euraPlayer = EuraAPI.getInstance().getEuraPlayer(player.getUniqueId());

        BankManager.getInstance().loadBank(player);
        player.teleport(ThePit.getInstance().getRandomSpawnPoint());
        if (!player.hasPlayedBefore())
            ThePit.getInstance().initPlayer(player);
        // load stats
        PlayerStatisticsManager.getInstance().loadPlayerStatistic(player);
        // update display name
        ThePit.getInstance().getServer().getOnlinePlayers().forEach(target -> DisplayNameManager.getInstance().update(target));
        // create scoreboard
        euraPlayer.createScoreBoard("thepit");
        ScoreboardManager.getInstance().register(euraPlayer);

        event.setJoinMessage(null);

    }

    // PlayerQuitEvent
    @EventHandler (priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        Bukkit.getOnlinePlayers().forEach(target -> DisplayNameManager.getInstance().update(target));

        // save bank data
        BankManager.getInstance().saveBank(player);

        PlayerStatistic playerStatistic = PlayerStatisticsManager.getInstance().getPlayerStatistic(player);
        // play session
        playerStatistic.addPlaySession(playerStatistic.getConnectedAt(), System.currentTimeMillis());
        // remove assists damage
        PlayerStatisticsManager.getInstance().removeAssistsDamage(playerStatistic);
        // save stats
        PlayerStatisticsManager.getInstance().savePlayerStatistic(playerStatistic);
        ScoreboardManager.getInstance().remove(player);

        event.setQuitMessage(null);

    }

    // On Player Pickup Item
    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {

        Player player = event.getPlayer();

        ItemStack item = event.getItem().getItemStack();

        if (item.getType() == Material.GOLD_INGOT) {

            Bank bank = BankManager.getInstance().getBank(player);
            BigDecimal goldReward = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(item.getAmount()));

            PickupGoldEvent pickupGoldEvent = new PickupGoldEvent(player, goldReward);
            Bukkit.getPluginManager().callEvent(pickupGoldEvent);

            if (!pickupGoldEvent.isCancelled()) {

                bank.pay(pickupGoldEvent.getGoldReward());
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
                player.sendMessage("§6§lGOLD! §r§7Ramassage au sol de §6" + pickupGoldEvent.getGoldReward() + "g");
                event.getItem().remove();
                event.setCancelled(true);

            }

        }

    }

    // On Damage
    @EventHandler (priority = EventPriority.LOWEST)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getEntity().getType() == EntityType.PLAYER) {

            System.out.print("before: " + event.getDamage());

            Player damaged = (Player) event.getEntity();

            Player damager = null;

            if (event.getDamager().getType() == EntityType.PLAYER)
                damager = (Player) event.getDamager();
            else if (event.getDamager().getType() == EntityType.ARROW && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
                damager = (Player) ((Arrow) event.getDamager()).getShooter();
                PlayerShootPlayerEvent playerShootPlayerEvent = new PlayerShootPlayerEvent(damager, damaged, event.getFinalDamage(), ((Arrow) event.getDamager()).isCritical());
                Bukkit.getPluginManager().callEvent(playerShootPlayerEvent);
                damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
            }

            if (damager != null) {

                PlayerStatisticsManager.getInstance().getPlayerStatistic(damaged).setLastAttackDate(System.currentTimeMillis());
                PlayerStatisticsManager.getInstance().getPlayerStatistic(damager).setLastAttackDate(System.currentTimeMillis());

                if (damaged.getHealth() - event.getFinalDamage() <= 0.0D) {

                    event.setCancelled(true);
                    ThePit.kill(damaged, damager);

                }

            }

        }

    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if (event.getEntity().getType() == EntityType.PLAYER) {

            Player player = (Player) event.getEntity();

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL || AreaManager.getInstance().isInSpawn(player.getLocation())) {
                
                event.setCancelled(true);

            } else if (player.getHealth() - event.getFinalDamage() <= 0.0D)  {

                ThePit.kill(player, player.getKiller());
                event.setCancelled(true);

            }

        }

    }

    @EventHandler
    public void onEarnExp(EarnExperienceEvent event) {

        Player player = event.getPlayer();

        DisplayNameManager.getInstance().update(player);

        if (event.getLevelPassed() >= 1) {

            Bank bank = event.getBank();

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);

            Level currentLevel = bank.getLevel();
            Level previousLevel = Level.getLevel(currentLevel.level - event.getLevelPassed());

            String color = bank.getPrestige().getColor();
            String text = color + "[" + previousLevel.getLevel() + color + "] §7➟ " + color + "[" + currentLevel.getLevel() + color + "]";
            Title.sendTitle(player, 10, 30, 10, "§b§lLEVEL UP", text);
            player.sendMessage("§b§lLEVEL UP! §7" + text);

        }

    }

    // On Player Talks
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        event.setFormat("%s§7: %s");

        if (!player.isOp())
            return;

        if (event.getMessage().startsWith("xp")) {
            BankManager.getInstance().getBank(player).addExperience(new BigDecimal(event.getMessage().split(Pattern.quote(" "))[1]));
        } else if (event.getMessage().startsWith("gold")) {
            BankManager.getInstance().getBank(player).pay(new BigDecimal(event.getMessage().split(Pattern.quote(" "))[1]));
        } else if (event.getMessage().startsWith("events time")) {
            ThePit.getInstance().getGameEventManager().getEventsCalendar().entrySet().stream()
                    .sorted(Comparator.comparingLong(Map.Entry::getKey))
                    .forEach(entry -> {
                        String date = new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date(entry.getKey()));
                        player.sendMessage(" - " + date + " §e§l" + entry.getValue().name().toUpperCase(Locale.ROOT));
            });
        } else if (event.getMessage().startsWith("events start")) {
            ThePit.getInstance().getGameEventManager().startNextGame();
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Area areaFrom = AreaManager.getInstance().getArea(event.getFrom());
        Area areaTo = AreaManager.getInstance().getArea(event.getTo());

        if (areaFrom != areaTo)
            Bukkit.getPluginManager().callEvent(new PlayerChangeAreaEvent(player, areaFrom, areaTo));

    }

    // On Block Break
    @EventHandler
    public void onBreak(BlockBreakEvent event) {

    }

    // On Player Places Block
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

    }

    // On Food Level Changes
    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {

        event.setCancelled(true);

    }

    @EventHandler
    public void onPhysics(BlockPhysicsEvent event) {

        if (event.getChangedType() == Material.LAVA || event.getChangedType() == Material.WATER)
            event.setCancelled(true);

    }

}
