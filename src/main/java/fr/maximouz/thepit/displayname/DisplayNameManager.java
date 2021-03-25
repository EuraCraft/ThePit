package fr.maximouz.thepit.displayname;

import fr.maximouz.thepit.Tag.TagManager;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.bank.BankManager;
import fr.maximouz.thepit.bank.Prestige;
import fr.maximouz.thepit.prime.Prime;
import fr.maximouz.thepit.Tag.Tag;
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
        TagManager.getInstance().getTags().add(new Tag(player, prefix, bank.getPrestige().getPriority() + bank.getLevel().getPriority(), primeText));

    }

}
