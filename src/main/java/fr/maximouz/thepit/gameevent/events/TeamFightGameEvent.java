package fr.maximouz.thepit.gameevent.events;

import fr.maximouz.thepit.events.PlayerInitEvent;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.gameevent.GameEvent;
import fr.maximouz.thepit.gameevent.GameEventType;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TeamFightGameEvent extends GameEvent {

    private final List<UUID> redTeam;
    private final List<UUID> blueTeam;
    private final Map<UUID, Integer> kills;
    private final Map<UUID, ItemStack> playerPreviousHelmets;

    public TeamFightGameEvent() {
        super(GameEventType.TEAM_FIGHT);
        redTeam = new ArrayList<>();
        blueTeam = new ArrayList<>();
        kills = new HashMap<>();
        playerPreviousHelmets = new HashMap<>();
    }

    private void splitPlayers() {
        int i = 1;
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerPreviousHelmets.put(player.getUniqueId(), player.getInventory().getHelmet());
            if (i++ % 2 == 0) {
                redTeam.add(player.getUniqueId());
                player.sendMessage("§cVous êtes dans l'équipe §lrouge§c!");
                player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()));
            } else {
                blueTeam.add(player.getUniqueId());
                player.sendMessage("§9Vous êtes dans l'équipe §lbleue§9!");
                player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData()));
            }
        }
    }

    private boolean isRed(UUID playerUniqueId) {
        return redTeam.contains(playerUniqueId);
    }

    private boolean isBlue(UUID playerUniqueId) {
        return blueTeam.contains(playerUniqueId);
    }

    public int getRedTeamKills() {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        kills.forEach((playerUid, playerKills) -> {
            if (isRed(playerUid))
                count.set(count.get() + playerKills);
        });
        return count.get();
    }

    public int getBlueTeamKills() {
        AtomicReference<Integer> count = new AtomicReference<>(0);
        kills.forEach((playerUid, playerKills) -> {
            if (isBlue(playerUid))
                count.set(count.get() + playerKills);
        });
        return count.get();
    }

    public List<Map.Entry<UUID, Integer>> getOrderedKills() {
        List<Map.Entry<UUID, Integer>> kills = new ArrayList<>(this.kills.entrySet());
        return kills.stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toList());
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER)
            return;
        UUID uid1 = event.getEntity().getUniqueId();
        UUID uid2 = event.getDamager().getUniqueId();
        if ((isRed(uid1) && isRed(uid2)) || (isBlue(uid1) && isBlue(uid2)))
            event.setCancelled(true);
    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {
        Player killer = event.getPlayer();
        if (isRed(killer.getUniqueId()) || isBlue(killer.getUniqueId())) {
            event.getDrops().removeIf(item -> item.getType().equals(Material.WOOL));
            kills.put(killer.getUniqueId(), kills.getOrDefault(killer.getUniqueId(), 0) + 1);
        }
    }

    @EventHandler
    public void onPlayerInit(PlayerInitEvent event) {
        Player player = event.getPlayer();
        if (isRed(player.getUniqueId())) player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()));
        else if (isBlue(player.getUniqueId())) player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (redTeam.size() > blueTeam.size()) {
            redTeam.add(player.getUniqueId());
            player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData()));
        } else {
            blueTeam.add(player.getUniqueId());
            player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData()));
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onClickHelmet(InventoryClickEvent event) {
        if (event.getSlot() == 103)
            event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getInventory().setHelmet(playerPreviousHelmets.getOrDefault(player.getUniqueId(), player.getInventory().getHelmet()));
        playerPreviousHelmets.remove(player.getUniqueId());
    }

    @Override
    public void start() {
        splitPlayers();
    }

    @Override
    public void end() {
        int redKills = getRedTeamKills();
        int blueKills = getBlueTeamKills();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().setHelmet(playerPreviousHelmets.getOrDefault(player.getUniqueId(), player.getInventory().getHelmet()));
            playerPreviousHelmets.remove(player.getUniqueId());
        });

        Bukkit.broadcastMessage("§eRésultat: " + (redKills > blueKills ? "l'équipe §cRouge§e l'emporte avec §4" + redKills + " kills !" : blueKills > redKills ? "l'équipe §9Bleue§e l'emporte avec §4" + redKills + " kills !" : "§fégalité !"));
        List<Map.Entry<UUID, Integer>> orderedKills = getOrderedKills();
        if (orderedKills.size() > 0) {

            Map.Entry<UUID, Integer> entry = orderedKills.get(0);
            UUID playerUid = entry.getKey();
            int playerKills = entry.getValue();
            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerUid);
            Bukkit.broadcastMessage(" §e#1. " + (isRed(playerUid) ? "§c" : "§9") + offPlayer.getName() + "§7(" + playerKills + " kills)");

            if (orderedKills.size() > 1) {

                entry = orderedKills.get(1);
                playerUid = entry.getKey();
                playerKills = entry.getValue();
                offPlayer = Bukkit.getOfflinePlayer(playerUid);
                Bukkit.broadcastMessage(" §e#2. " + (isRed(playerUid) ? "§c" : "§9") + offPlayer.getName() + "§7(" + playerKills + " kills)");

                if (orderedKills.size() > 2) {

                    entry = orderedKills.get(1);
                    playerUid = entry.getKey();
                    playerKills = entry.getValue();
                    offPlayer = Bukkit.getOfflinePlayer(playerUid);
                    Bukkit.broadcastMessage(" §e#3. " + (isRed(playerUid) ? "§c" : "§9") + offPlayer.getName() + "§7(" + playerKills + " kills)");

                }

            }
        }
    }
}
