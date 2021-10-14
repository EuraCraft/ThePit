package fr.maximouz.thepit.inventories.quest;

import fr.euracraft.api.gui.AbstractInterface;
import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.quest.Quest;
import fr.maximouz.thepit.quest.QuestManager;
import fr.maximouz.thepit.utils.SkinUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class QuestInventory extends AbstractInterface {

    public QuestInventory(Player owner) {
        super(owner, "§8Quêtes & Contrats", 5*9);

        QuestManager questManager = ThePit.getInstance().getQuestManager();

        ItemStack helpItem = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) helpItem.getItemMeta();
        SkinUtils.applyQuestionMarkHead(meta);
        meta.setDisplayName("§aQuêtes & Contrats");
        meta.setLore(Arrays.asList(
                "§7Regardez les quêtes et contrats",
                "§7que vous pouvez compléter en",
                "§7jouant sur le serveur."
        ));
        helpItem.setItemMeta(meta);

        this.setItem(4, helpItem, event -> owner.playSound(owner.getLocation(), Sound.VILLAGER_IDLE, 1f, 1f));

        int slot = 20;

        for (Quest quest : questManager.getQuests()) {

            this.setItem(slot++, questManager.getItem(quest, owner), event -> owner.playSound(owner.getLocation(), Sound.NOTE_STICKS, 1f, 1f));

        }

    }

}
