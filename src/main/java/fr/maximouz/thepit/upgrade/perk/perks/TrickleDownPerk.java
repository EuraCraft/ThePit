package fr.maximouz.thepit.upgrade.perk.perks;

import fr.euracraft.api.util.SymbolUtil;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.EarnGoldEvent;
import fr.maximouz.thepit.events.EarnReason;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class TrickleDownPerk extends Perk {

    public TrickleDownPerk() {
        super(PerkType.TRICKLE_DOWN, "Jackpot", Material.GOLD_INGOT, Level.THIRTY, 3000, ChatColor.GRAY + "Les lingots d'or rapportent " + ChatColor.GOLD + "10g " + ChatColor.GRAY + "en plus", ChatColor.GRAY + "et vous soigne " + ChatColor.RED + "2" + SymbolUtil.HEARTH + ChatColor.GRAY + ".");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @EventHandler
    public void onPickupGold(EarnGoldEvent event) {

        if (event.getReason() != EarnReason.PICK_UP)
            return;

        if (hasSelected(event.getPlayer())) {

            event.setAmount(event.getAmount() + 10);
            Player player = event.getPlayer();
            player.setHealth(Math.min(player.getHealth() + 4, 20));

        }

    }

}
