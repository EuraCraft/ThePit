package fr.maximouz.thepit.quest;

import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.math.BigDecimal;
import java.util.*;

public abstract class Quest implements IQuest, Listener {

    private final QuestType type;

    private final Map<UUID, Long> progressions;

    public Quest(QuestType type) {
        this.type = type;
        progressions = new HashMap<>();
    }

    public QuestType getType() {
        return type;
    }

    @Override
    public QuestTime getTime() {
        return type.getQuestTime();
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public String getDescription() {
        return type.getDescription();
    }

    @Override
    public long getAmount() {
        return type.getAmount();
    }

    @Override
    public BigDecimal getReward() {
        return type.getReward();
    }

    public void resetProgressions() {
        progressions.clear();
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {
        progressions.remove(player.getUniqueId());
    }

    @Override
    public void complete(Player player) {
        Bank bank = BankManager.getInstance().getBank(player);
        bank.pay(getReward());
        player.sendMessage("§2Quête " + getTime().getStringValue() + " terminée !");
        player.sendMessage("§a" + getName() + "\n §7" + getDescription() + "\n §6+" + getReward().toString() + "g");
        //player.sendMessage("§b§lCONTRAT COMPLÉTÉ! §r§6+" + getReward().toString() + "g");
    }

    public boolean isInCoolDown(UUID uuid) {
        return getProgression(uuid) > getAmount();
    }

    public boolean isInCoolDown(Player player) {
        return isInCoolDown(player.getUniqueId());
    }

    public long getProgression(UUID uuid) {
        return progressions.getOrDefault(uuid, 0L);
    }

    public long getProgression(Player player) {
        return getProgression(player.getUniqueId());
    }

    public void setProgression(UUID uuid, long progression) {
        progressions.put(uuid, progression);
        if (progression >= getAmount())
            complete(Bukkit.getPlayer(uuid));
    }

    public void setProgression(Player player, long progression) {
        progressions.put(player.getUniqueId(), progression);
        if (progression >= getAmount())
            complete(player);
    }

    public void increaseProgression(UUID uuid) {
        setProgression(uuid, getProgression(uuid) + 1);
    }

    public void increaseProgression(Player player) {
        increaseProgression(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        load(event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        save(event.getPlayer());

    }

}
