package fr.maximouz.thepit.upgrade.upgrades;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.TempBlockPlaceEvent;
import fr.maximouz.thepit.upgrade.Upgrade;
import fr.maximouz.thepit.upgrade.UpgradeType;
import fr.maximouz.thepit.utils.Format;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class BuilderUpgrade extends Upgrade {

    /**
     * Integer 1 : tier
     * Integer 2 : Block life time multiplier
     */
    private final Map<Integer, Integer> tiersMultiplier;

    public BuilderUpgrade(UpgradeType type) {
        super(type);
        tiersMultiplier = new HashMap<>();

        tiersMultiplier.put(1, 60);
        setPrice(1, 750.0);
        setLevelRequired(1, Level.ONE);

        tiersMultiplier.put(2, 96);
        setPrice(2, 1750.0);
        setLevelRequired(2, Level.ONE);

        tiersMultiplier.put(3, 154);
        setPrice(3, 2750.0);
        setLevelRequired(3, Level.ONE);

        tiersMultiplier.put(4, 245);
        setPrice(4, 3750.0);
        setLevelRequired(4, Level.SEVENTY);

        tiersMultiplier.put(5, 393);
        setPrice(5, 4750.0);
        setLevelRequired(5, Level.SEVENTY);

        /*tiersMultiplier.put(6, 360);
        setPrice(6, 6000.0);
        setLevelRequired(6, Level.SEVENTY);*/

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
    public String getBonus(Player player) {
        int playerTier = getTier(player);
        int tierBlockLifeTime = tiersMultiplier.get(playerTier == getMaxTier() ? playerTier : playerTier + 1);
        return ChatColor.GREEN + "+" + tierBlockLifeTime + "%";
    }

    @Override
    public short getDyeColor() {
        // white
        return (short) 15;
    }

    @Override
    public int getMaxTier() {
        return tiersMultiplier.size();
    }

    public double getTierMultiplier(int tier) {
        return 1 + (tiersMultiplier.get(tier) / 100.0);
    }

    @EventHandler
    public void onTempBlockPlace(TempBlockPlaceEvent event) {

        Player player = event.getPlayer();
        int playerTier = getTier(player);

        if (playerTier > 0) {

            event.setLifeTime(Format.roundToInt(getTierMultiplier(playerTier) * event.getLifeTime()));

        }

    }
}
