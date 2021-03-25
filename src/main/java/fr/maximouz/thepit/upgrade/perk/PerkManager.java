package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.ThePit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PerkManager {

    private static final PerkManager INSTANCE = new PerkManager();

    private final List<Perk> perks;

    public PerkManager() {
        this.perks = new ArrayList<>();

        for (PerkType perkType : PerkType.values()) {
            perks.add(perkType.getNewInstance());
        }
    }

    public static PerkManager getInstance() {
        return INSTANCE;
    }

    public List<Perk> getPerks() {
        return perks;
    }

    public Perk getPerk(PerkType type) {
        return perks.stream().filter(perk -> perk.getType() == type).findFirst().orElse(null);
    }

    public Perk getPlayerPerk(Player player, PerkSlot perkSlot) {
        return perks.stream().filter(perk -> perk.hasSelected(player) && perk.getSelectedSlot(player) == perkSlot).findFirst().orElse(null);
    }

    public List<Perk> getSelectedPerk(Player player) {
        List<Perk> perks = new ArrayList<>();
        for (PerkSlot perkSlot : PerkSlot.values()) {
            Perk perk = getPlayerPerk(player, perkSlot);
            if (perk != null) {
                perks.add(perk);
            }
        }
        return perks;
    }

    public void removePlayerPerk(Player player, PerkSlot perkSlot) {
        Perk perk = getPlayerPerk(player, perkSlot);
        if (perk != null) {
            perk.removeSelect(player);
        }
    }

    public void registerListeners() {
        perks.forEach(perk -> Bukkit.getPluginManager().registerEvents(perk, ThePit.getInstance()));
    }

}
