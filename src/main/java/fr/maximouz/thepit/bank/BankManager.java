package fr.maximouz.thepit.bank;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankManager {

    private static final BankManager INSTANCE = new BankManager();

    private final List<Bank> banks;

    public BankManager() {
        this.banks = new ArrayList<>();
    }

    public static BankManager getInstance() {
        return INSTANCE;
    }

    /**
     * Charger la banque d'un joueur
     * @param player Player we want to load the bank
     */
    public void loadBank(Player player) {
        banks.add(new Bank(player, 99999, 0, 0, Level.ONE, Prestige.ZERO));
    }

    /**
     * Sauvegarder en base de données la banque d'un joueur
     * @param player Player we want to save the bank
     */
    public void saveBank(Player player) {
        Bank bank = getBank(player);
        // save in db
        banks.remove(bank);
    }

    /**
     * Récupérer la banque d'un joueur avec son identifiant unique
     * @param uuid Player Unique Id
     * @return Bank or null if not found
     */
    public Bank getBank(UUID uuid) {
        return banks.stream()
                .filter(bank -> bank.getOwner().getUniqueId() == uuid)
                .findFirst()
                .orElse(null);
    }

    /**
     * Récupérer la banque d'un joueur
     * @param player Player
     * @return Bank or null if not found
     */
    public Bank getBank(Player player) {
        return getBank(player.getUniqueId());
    }

}
