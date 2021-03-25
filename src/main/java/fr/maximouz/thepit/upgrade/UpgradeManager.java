package fr.maximouz.thepit.upgrade;

import fr.maximouz.thepit.ThePit;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class UpgradeManager {

    private static final UpgradeManager INSTANCE = new UpgradeManager();

    private final List<Upgrade> upgrades;

    public UpgradeManager() {
        this.upgrades = new ArrayList<>();

        for (UpgradeType type : UpgradeType.values())
            upgrades.add(type.getNewInstance());

    }

    public static UpgradeManager getInstance() {
        return INSTANCE;
    }

    public void registerListeners() {
        upgrades.forEach(upgrade -> {

            upgrade.loadAll();
            Bukkit.getPluginManager().registerEvents(upgrade, ThePit.getInstance());

        });
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Upgrade getUpgrade(UpgradeType type) {
        return upgrades.stream().filter(upgrade -> upgrade.getType() == type).findFirst().orElse(null);
    }

}
