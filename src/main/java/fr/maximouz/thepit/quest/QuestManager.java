package fr.maximouz.thepit.quest;

import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuestManager {

    private final List<Quest> quests;
    private final Map<QuestTime, Long> resetAt;

    public QuestManager() {

        quests = new ArrayList<>();
        resetAt = new HashMap<>();

        for (QuestType type : QuestType.values()) {
            Quest quest = type.getNewInstance();
            if (quest != null) {
                quests.add(quest);
                Bukkit.getPluginManager().registerEvents(quest, ThePit.getInstance());
            }
        }

        for (QuestTime questTime : QuestTime.values())
            loadResetTask(questTime);

    }

    public List<Quest> getQuests() {
        return quests;
    }

    public void loadResetTask(QuestTime questTime) {

        ThePit.getInstance().getConfig().addDefault("times." + questTime.toString().toLowerCase() + ".reset_at", System.currentTimeMillis() + questTime.getCoolDown());
        long resetAt = ThePit.getInstance().getConfig().getLong("times." + questTime.toString().toLowerCase() + ".reset_at");

        if (resetAt - System.currentTimeMillis() <= 0)
            resetAt = System.currentTimeMillis() + questTime.getCoolDown();

        ThePit.getInstance().getConfig().set("times." + questTime.toString().toLowerCase() + ".reset_at", resetAt);
        ThePit.getInstance().saveConfig();

        this.resetAt.put(questTime, resetAt);
        Bukkit.getScheduler().runTaskLaterAsynchronously(ThePit.getInstance(), () -> {

            resetProgressions(questTime);
            loadResetTask(questTime);

        }, 20 * TimeUnit.MILLISECONDS.toSeconds(questTime.getCoolDown()));

    }

    public void resetProgressions(QuestTime questTime) {
        quests.forEach(quest -> {
            if (quest.getTime() == questTime)
                quest.resetProgressions();
        });
    }

    public long getCoolDownLeft(QuestTime questTime) {
        return resetAt.get(questTime) - System.currentTimeMillis();
    }

    public ItemStack getItem(Quest quest, Player player) {
        ItemBuilder item = new ItemBuilder(Material.PAPER);

        long coolDownLeft = getCoolDownLeft(quest.getTime());
        long progression = quest.getProgression(player);
        boolean isInCoolDown = progression >= quest.getAmount();

        item.setName("§2Quête " + quest.getTime().getStringValue());

        item.setLore(
                "§a" + quest.getName(),
                "",
                "§7" + quest.getDescription(),
                "§7Récompense: §6" + Format.format(quest.getReward()) + "g",
                "",
                isInCoolDown ? "§cCooldown: " + Format.time(coolDownLeft) : "§7Progression: §b" + progression + "§7/" + quest.getAmount()
        );

        if (!isInCoolDown) {
            item.addEnchant(Enchantment.DIG_SPEED, 1);
            item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        return item.build();
    }

}
