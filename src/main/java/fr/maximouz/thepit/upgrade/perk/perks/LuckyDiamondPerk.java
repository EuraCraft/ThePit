package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerDeathDropIronArmorPieceEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import fr.maximouz.thepit.utils.ItemStackUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class LuckyDiamondPerk extends Perk {


    public LuckyDiamondPerk() {
        super(PerkType.LUCKY_DIAMOND, "Diamant chanceux", Material.DIAMOND, Level.THIRTY, 1000.0, ChatColor.GRAY + "Vous avez 30% de chances de récuperer une pièce", ChatColor.GRAY + "d'armure en " + ChatColor.AQUA + "diamant" + ChatColor.GRAY + " lorsque vous réalisez un meurtre.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @EventHandler
    public void onDrop(PlayerDeathDropIronArmorPieceEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            if (Math.random() <= 0.30) {

                ItemStack item = event.getItem();

                if (ItemStackUtils.equip(player, item, true)) {

                    event.setCancelled(true);

                }

            }

        }

    }

}
