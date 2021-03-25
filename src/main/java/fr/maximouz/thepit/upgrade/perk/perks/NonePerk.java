package fr.maximouz.thepit.upgrade.perk.perks;

import fr.maximouz.thepit.bank.Level;
import fr.maximouz.thepit.upgrade.perk.Perk;
import fr.maximouz.thepit.upgrade.perk.PerkType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NonePerk extends Perk {

    public NonePerk() {
        super(PerkType.NONE, "none", ChatColor.RED + "Aucune compétence", Material.DIAMOND_BLOCK, Level.ONE, 0, ChatColor.GRAY + "Rétire la compétence de cet emplacement,", ChatColor.GRAY + "cependant vous devrez redoubler d'efforts lors", ChatColor.GRAY + "de vos combats !", "", ChatColor.YELLOW + "Clic gauche pour retirer la compétence.");
    }

    @Override
    public void load(Player player) {
        buy(player);
    }

    @Override
    public void save(Player player) {}
}
