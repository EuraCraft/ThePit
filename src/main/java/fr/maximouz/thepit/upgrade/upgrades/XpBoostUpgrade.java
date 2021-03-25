package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class XpBoostUpgrade extends Upgrade {

    private final Map<Integer, Integer> tiersXp;

    public XpBoostUpgrade() {
        super(UpgradeType.XP_BOOST, "xpboost", ChatColor.DARK_AQUA + "Boost XP", ChatColor.GRAY + "Vous gagnez " + ChatColor.AQUA + "10% d'XP" + ChatColor.GRAY + " supplémentaires", ChatColor.GRAY + "lorsque vous réalisez un meurtre", ChatColor.GRAY + "ou une assistance.");

        tiersXp = new HashMap<>();

        tiersXp.put(1, 10);
        setPrice(1, 10.0);
        setLevelRequired(1, Level.ONE);

        tiersXp.put(2, 20);
        setPrice(2, 20.0);
        setLevelRequired(2, Level.TWO);

        tiersXp.put(3, 20);
        setPrice(2, 20.0);
        setLevelRequired(2, Level.EIGHTEEN);
    }

    @Override
    public void load(Player player) {
        setTier(player, 0);
    }

    @Override
    public void save(Player player) {
        removeTier(player);
    }

    @Override
    public int getMaxTier() {
        return tiersXp.size();
    }

    @Override
    public String getBonus(Player player) {
        int playerTier = getTier(player);
        int tierXp = tiersXp.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.AQUA + "+" + tierXp + "% XP";
    }

    @Override
    public short getDyeColor() {
        // light blue
        return (short) 12;
    }
}
