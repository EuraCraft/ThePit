package fr.maximouz.thepit.displayname;

import fr.maximouz.thepit.tag.TagManager;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.bank.Prestige;
import fr.maximouz.thepit.prime.Prime;
import fr.maximouz.thepit.tag.Tag;
import fr.maximouz.thepit.prime.PrimeManager;
import org.bukkit.entity.Player;

public class DisplayNameManager {

    private static final DisplayNameManager INSTANCE = new DisplayNameManager();

    public DisplayNameManager() { }

    public static DisplayNameManager getInstance() {
        return INSTANCE;
    }

    public void update(Player player) {

        Bank bank = BankManager.getInstance().getBank(player);
        Prime prime = PrimeManager.getInstance().getPrime(player);

        String prestigeColor = bank.getPrestige().getColor();
        String prestige = bank.getPrestige() != Prestige.ZERO ? bank.getPrestige().getName() + prestigeColor + "-" : "";
        String primeText = prime != null ? " §6§l" + prime.getGold() + "g" : "";
        String prefix = prestigeColor + "[" + bank.getLevel().getLevel() + prestigeColor + "] §7";

        // Chat
        player.setDisplayName(prestigeColor + "[" + prestige + bank.getLevel().getLevel() + prestigeColor + "] §7" + player.getName());
        // Tab
        player.setPlayerListName(prefix + player.getName() + primeText);
        // Above Head
        Tag playerTag = TagManager.getInstance().getTag(player);
        String priority = bank.getLevel().getPriority();
        if (playerTag == null) {
            playerTag = new Tag(player, prefix, priority, primeText);
            TagManager.getInstance().getTags().add(playerTag);
        } else {
            playerTag.setPrefix(prefix);
            playerTag.setPriority(priority);
            playerTag.setSuffix(primeText);
        }
        playerTag.settingTab();

    }

}
