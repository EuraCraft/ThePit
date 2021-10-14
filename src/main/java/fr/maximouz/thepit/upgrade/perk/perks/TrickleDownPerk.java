package fr.maximouz.thepit.upgrade.perk.perks;

import fr.euracraft.api.util.SymbolUtil;
import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PickupGoldEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.math.BigDecimal;

public class TrickleDownPerk extends Perk {

    public TrickleDownPerk() {
        super(PerkType.TRICKLE_DOWN, "Jackpot", Material.GOLD_INGOT, Level.THIRTY, 3000,
                "§7Les lingots d'or rapportent §610g§7 en plus",
                "§7et vous soigne §c2" + SymbolUtil.HEARTH + "§7.");
    }

    @EventHandler
    public void onPickupGold(PickupGoldEvent event) {

        if (hasSelected(event.getPlayer())) {

            event.setGoldReward(event.getGoldReward().add(BigDecimal.valueOf(10)));
            Player player = event.getPlayer();
            player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + 4));

        }

    }

}
