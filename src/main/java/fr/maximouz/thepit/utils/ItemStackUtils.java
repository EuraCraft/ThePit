package fr.maximouz.thepit.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemStackUtils {

    public static boolean isLeatherArmorPiece(Material material) {
        return material.getId() >= 298 && material.getId() <= 301;
    }

    public static boolean isLeatherArmorPiece(ItemStack item) {
        return isLeatherArmorPiece(item.getType());
    }


    public static boolean isChainMailArmorPiece(Material material) {
        return material.getId() >= 302 && material.getId() <= 305;
    }

    public static boolean isChainMailArmorPiece(ItemStack item) {
        return isChainMailArmorPiece(item.getType());
    }


    public static boolean isIronArmorPiece(Material material) {
        return material.getId() >= 306 && material.getId() <= 309;
    }

    public static boolean isIronArmorPiece(ItemStack item) {
        return isIronArmorPiece(item.getType());
    }


    public static boolean isDiamondArmorPiece(Material material) {
        return material.getId() >= 310 && material.getId() <= 313;
    }

    public static boolean isDiamondArmorPiece(ItemStack item) {
        return isDiamondArmorPiece(item.getType());
    }

    public static boolean isHelmet(Material material) {
        return material.toString().contains("HELMET");
    }

    public static boolean isHelmet(ItemStack item) {
        return isHelmet(item.getType());
    }

    public static boolean isChestPlate(Material material) {
        return material.toString().contains("CHESTPLATE");
    }

    public static boolean isChestPlate(ItemStack item) {
        return isChestPlate(item.getType());
    }

    public static boolean isLeggings(Material material) {
        return material.toString().contains("LEGGINGS");
    }

    public static boolean isLeggings(ItemStack item) {
        return isLeggings(item.getType());
    }

    public static boolean isBoots(Material material) {
        return material.toString().contains("BOOTS");
    }

    public static boolean isBoots(ItemStack item) {
        return isBoots(item.getType());
    }

    public static boolean hasHelmet(PlayerInventory inventory) {
        return inventory.getHelmet() != null && inventory.getHelmet().getType() != Material.AIR;
    }

    public static boolean hasHelmet(Player player) {
        return hasHelmet(player.getInventory());
    }

    public static boolean hasChestPlate(PlayerInventory inventory) {
        return inventory.getChestplate() != null && inventory.getChestplate().getType() != Material.AIR;
    }

    public static boolean hasChestPlate(Player player) {
        return hasChestPlate(player.getInventory());
    }

    public static boolean hasLeggings(PlayerInventory inventory) {
        return inventory.getLeggings() != null && inventory.getLeggings().getType() != Material.AIR;
    }

    public static boolean hasLeggings(Player player) {
        return hasLeggings(player.getInventory());
    }

    public static boolean hasBoots(PlayerInventory inventory) {
        return inventory.getBoots() != null && inventory.getBoots().getType() != Material.AIR;
    }

    public static boolean hasBoots(Player player) {
        return hasBoots(player.getInventory());
    }

    public static int reversedFirstEmpty(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        for (int i = 9; i < 36; i++)
            if (contents[i] == null)
                return i;

        for (int i = 8; i >= 0; i--)
            if (contents[i] == null)
                return i;

        return 0;
    }

    public static int reversedFirstEmpty(Player player) {
        return reversedFirstEmpty(player.getInventory());
    }

    public static boolean equip(Player player, ItemStack item, boolean playSounds) {

        System.out.print("equiping " + player.getName());

        if (item == null || item.getType() == Material.AIR)
            return false;

        if (isHelmet(item)) {

            if (hasHelmet(player)) {

                int index = reversedFirstEmpty(player);
                if (index == -1)
                    return false;

                // Si l'équipement ramassé est moins bien que celui équipé
                if (item.getType().getId() <= player.getInventory().getHelmet().getType().getId()) {

                    player.getInventory().setItem(index, item);
                    item = player.getInventory().getHelmet();

                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);

                } else {

                    player.getInventory().setItem(index, player.getInventory().getHelmet());
                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);

                }

            }

            player.getInventory().setHelmet(item);
            return true;

        }

        if (isChestPlate(item)) {

            if (hasChestPlate(player)) {

                int index = reversedFirstEmpty(player);
                if (index == -1)
                    return false;

                // Si l'équipement ramassé est moins bien que celui équipé
                if (item.getType().getId() <= player.getInventory().getChestplate().getType().getId()) {

                    player.getInventory().setItem(index, item);
                    item = player.getInventory().getChestplate();

                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);

                } else {

                    player.getInventory().setItem(index, player.getInventory().getChestplate());
                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);

                }

            }

            player.getInventory().setChestplate(item);
            return true;

        }

        if (isLeggings(item)) {

            if (hasLeggings(player)) {

                int index = reversedFirstEmpty(player);
                if (index == -1)
                    return false;

                // Si l'équipement ramassé est moins bien que celui équipé
                if (item.getType().getId() <= player.getInventory().getLeggings().getType().getId()) {

                    player.getInventory().setItem(index, item);
                    item = player.getInventory().getLeggings();

                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);

                } else {

                    player.getInventory().setItem(index, player.getInventory().getLeggings());
                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);

                }

            }

            player.getInventory().setLeggings(item);
            return true;

        }

        if (isBoots(item)) {

            if (hasBoots(player)) {

                int index = reversedFirstEmpty(player);
                if (index == -1)
                    return false;

                // Si l'équipement ramassé est moins bien que celui équipé
                if (item.getType().getId() <= player.getInventory().getBoots().getType().getId()) {

                    player.getInventory().setItem(index, item);
                    item = player.getInventory().getBoots();
                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1f, 1f);

                } else {

                    player.getInventory().setItem(index, player.getInventory().getBoots());
                    if (playSounds)
                        player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1f, 1f);

                }

            }

            player.getInventory().setBoots(item);
            return true;

        }

        return false;
    }

    public static boolean isSword(Material material) {
        return material.toString().contains("SWORD");
    }

    public static boolean isSword(ItemStack item) {
        return isSword(item.getType());
    }

    public static int count(ItemStack[] contents, Material material) {
        int count = 0;

        for (ItemStack item : contents)
            if (item != null && item.getType() == material)
                count++;

        return count;
    }

}
