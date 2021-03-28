package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.events.PlayerInitEvent;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class SecurityFirstPerk extends Perk {

    public SecurityFirstPerk() {
        super(PerkType.SECURITY_FIRST, "Aucun risque", Material.CHAINMAIL_HELMET, Level.TWENTY, 3000, ChatColor.GRAY + "Vous apparaissez avec un", ChatColor.GRAY + "casque en maille.");
    }

    @Override
    public void load(Player player) {}

    @Override
    public void save(Player player) {}

    @Override
    public void onUnselect(Player player) {
        player.getInventory().setHelmet(null);
    }

    @EventHandler
    public void onInit(PlayerInitEvent event) {

        Player player = event.getPlayer();

        if (hasSelected(player)) {

            event.setHelmet(new ItemStack(getMaterial()));

        }

    }

}
