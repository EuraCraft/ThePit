package fr.maximouz.thepit.upgrade.perk;

import fr.maximouz.thepit.ThePit;
import fr.maximouz.thepit.events.KillStreakUpdateEvent;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerk;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkSlot;
import fr.maximouz.thepit.upgrade.perk.killstreaks.KillStreakPerkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerkManager implements Listener {

    private static final PerkManager INSTANCE = new PerkManager();

    private final List<Perk> perks;
    private final List<KillStreakPerk> killStreakPerks;

    public PerkManager() {
        this.perks = new ArrayList<>();
        this.killStreakPerks = new ArrayList<>();

        for (PerkType perkType : PerkType.values()) {
            perks.add(perkType.getNewInstance());
        }

        for (KillStreakPerkType ksPerkType : KillStreakPerkType.values()) {
            killStreakPerks.add(ksPerkType.getNewInstance());
        }

        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance());
    }

    public static PerkManager getInstance() {
        return INSTANCE;
    }

    public List<Perk> getPerks() {
        return perks;
    }

    public List<KillStreakPerk> getKillStreakPerks() {
        return killStreakPerks;
    }

    public Perk getPerk(PerkType type) {
        return perks.stream().filter(perk -> perk.getType() == type).findFirst().orElse(null);
    }

    public KillStreakPerk getKillStreakPerk(KillStreakPerkType type) {
        return killStreakPerks.stream().filter(perk -> perk.getType() == type).findFirst().orElse(null);
    }

    public Perk getPlayerPerk(Player player, PerkSlot perkSlot) {
        return perks.stream().filter(perk -> perk.hasSelected(player) && perk.getSelectedSlot(player) == perkSlot).findFirst().orElse(null);
    }

    public KillStreakPerk getPlayerKillStreakPerk(Player player, KillStreakPerkSlot slot) {
        return killStreakPerks.stream().filter(perk -> perk.hasSelected(player) && perk.getSelectedSlot(player) == slot).findFirst().orElse(null);
    }

    /**
     * Récupérer les Perk séléctionnées par un joueur
     * @param player Le joueur que nous voulons récupérer les compétences
     * @return Une List de tous les Perk séléctionnées par le joueur
     */
    public List<Perk> getSelectedPerks(Player player) {
        List<Perk> perks = new ArrayList<>();
        for (PerkSlot perkSlot : PerkSlot.values()) {
            Perk perk = getPlayerPerk(player, perkSlot);
            if (perk != null)
                perks.add(perk);
        }
        return perks;
    }

    /**
     * Récupérer les KillStreakPerk séléctionnées par un joueur
     * @param player Le joueur que nous voulons récupérer les compétences
     * @return Une List de tous les KillStreakPerk séléctionnées par le joueur
     */
    public List<KillStreakPerk> getSelectedKillStreakPerk(Player player) {
        List<KillStreakPerk> perks = new ArrayList<>();
        for (KillStreakPerkSlot perkSlot : KillStreakPerkSlot.values()) {
            KillStreakPerk perk = getPlayerKillStreakPerk(player, perkSlot);
            if (perk != null)
                perks.add(perk);
        }
        return perks;
    }

    /**
     * Désélectionner un Perk
     * @param player Joueur possédant le Perk
     * @param perkSlot Emplacement du Perk à désactiver
     */
    public void unselectPlayerPerk(Player player, PerkSlot perkSlot) {
        Perk perk = getPlayerPerk(player, perkSlot);
        if (perk != null)
            perk.unselect(player);
    }

    /**
     * Désélectionner un KillStreakPerk
     * @param player Joueur possédant le KillStreakPerk
     * @param perkSlot Emplacement du KillStreakPerk à désactiver
     */
    public void unselectPlayerKillStreakPerk(Player player, KillStreakPerkSlot perkSlot) {

        KillStreakPerk playerPerk = getPlayerKillStreakPerk(player, perkSlot);

        if (playerPerk != null)
            playerPerk.unselect(player);

    }

    /**
     * Sélectionner un KillStreakPerk (désélectionne automatiquement tous les KillStreakPerk avec le même KillStreakEvery).
     * @param player Joueur à qui active le KillStreakPerk
     * @param perkSlot Emplacement pour le KillStreakPerk
     */
    public void selectPlayerKillStreakPerk(Player player, KillStreakPerk perk, KillStreakPerkSlot perkSlot) {

        Arrays.stream(KillStreakPerkSlot.values()).forEach(currentPerkSlot -> {

            KillStreakPerk currentPerk = getPlayerKillStreakPerk(player, currentPerkSlot);

            if (currentPerk != null && (currentPerk == perk || perk.getType().getEvery() == currentPerk.getType().getEvery()))
                currentPerk.unselect(player);

        });

        perk.select(player, perkSlot);

    }

    public void registerListeners() {
        perks.forEach(perk -> Bukkit.getPluginManager().registerEvents(perk, ThePit.getInstance()));
        killStreakPerks.forEach(perk -> Bukkit.getPluginManager().registerEvents(perk, ThePit.getInstance()));
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onKsIncrease(KillStreakUpdateEvent event) {

        Player player = event.getPlayer();

        for (KillStreakPerkSlot slot : KillStreakPerkSlot.values()) {

            KillStreakPerk perk = getPlayerKillStreakPerk(player, slot);

            if (perk != null && event.getNewKillStreak() > 1 && event.getNewKillStreak() % perk.getType().getEvery().getIntegerValue() == 0) {

                perk.trigger(player);
                System.out.print("Trigger " + player.getName() + " perk on slot " + slot.getIntegerValue() + " (perk: " + perk.getType().toString() + ")");

            }

        }

    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {

        perks.forEach(perk -> perk.load(event.getPlayer()));
        killStreakPerks.forEach(perk -> perk.load(event.getPlayer()));

    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {

        perks.forEach(perk -> perk.save(event.getPlayer()));
        killStreakPerks.forEach(perk -> perk.save(event.getPlayer()));

    }

}
