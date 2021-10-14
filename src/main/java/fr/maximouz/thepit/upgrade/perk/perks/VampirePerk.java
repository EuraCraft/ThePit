package fr.maximouz.thepit.upgrade.perk.perks;

import fr.euracraft.api.util.SymbolUtil;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerKillEvent;
import fr.maximouz.thepit.events.PlayerReceiveGAppleEvent;
import fr.maximouz.thepit.events.PlayerShootPlayerEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkManager;
import fr.maximouz.thepit.upgrade.perk.PerkSlot;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VampirePerk extends Perk {

    public VampirePerk() {
        super(PerkType.VAMPIRE, "Vampire", Material.FERMENTED_SPIDER_EYE, Level.SIXTY, 4000.0,
                "§7Vous ne gagnez plus de §epommes dorées.",
                "§7Un coup soigne §c0.5" + SymbolUtil.HEARTH + "§7.",
                "§7Une flêche critique soigne §c1.5" + SymbolUtil.HEARTH +"§7.",
                "§7Les meurtres donnent §fRégénération I §7(8s)."
        );
    }

    @Override
    public void onSelected(Player player) {

        for (PerkSlot perkSlot : PerkSlot.values()) {

            Perk perk = PerkManager.getInstance().getPlayerPerk(player, perkSlot);
            if (perk != null && perk.getType() == PerkType.GOLDEN_HEADS) {

                PerkManager.getInstance().unselectPlayerPerk(player, perkSlot);
                player.sendMessage("§c§lATTENTION! §7La compétence §eTêtes en or§7 a été retirée à cause de son incompatibilité avec la compétence §cVampire§7.");
                return;

            }

        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        Player player = (Player) event.getDamager();

        if (hasSelected(player)) {

            System.out.print("vampire for " + player.getName() + " (corps à corps)");
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 1));

        }

    }

    @EventHandler
    public void onShoot(PlayerShootPlayerEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player) && event.isCritical()) {

            System.out.print("vampire for " + player.getName() + " (arrow)");
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 3));

        }

    }

    @EventHandler
    public void onKill(PlayerKillEvent event) {

        Player player = event.getPlayer();

        if (player != null && hasSelected(player)) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 8, 0));

        }

    }

    @EventHandler
    public void onReceiveGApple(PlayerReceiveGAppleEvent event) {

        if (hasSelected(event.getPlayer()))
            event.setCancelled(true);

    }

}
