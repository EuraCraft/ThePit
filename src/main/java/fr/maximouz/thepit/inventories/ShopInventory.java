package fr.maximouz.thepit.inventories;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.items.AntiBountiedItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ShopInventory extends AbstractInterface {

    public ShopInventory(Player owner, Bank bank) {
        super(owner, ChatColor.DARK_GRAY + "Items temporaires", 3*9);

        ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(ChatColor.YELLOW + "Épée en Diamant")
                .setLore("§9+20% de dégâts contre les joueurs", "§9ayant une prime.", "", "§7§oCet item tombe au sol", "§7§osi vous mourez.", "", "§7Prix: §e150g", bank.getBalance() >= 150 ? "§eClic gauche pour acheter." : "§cVous n'avez pas assez de gold !")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();

        this.setItem(11, item, event -> {

            owner.closeInventory();

            if (bank.getBalance() >= 150) {

                if (owner.getInventory().firstEmpty() != -1) {

                    owner.sendMessage("§a§lACHAT! §eÉpée en Diamant");
                    bank.withdraw(150);
                    owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                    owner.getInventory().remove(Material.IRON_SWORD);
                    owner.getInventory().addItem(new AntiBountiedItem().build());

                } else {

                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    owner.sendMessage("§cVotre inventaire est plein !");

                }

            } else {

                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                owner.sendMessage("§cVous n'avez pas assez de gold !");

            }
        });

        item = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .setName(ChatColor.YELLOW + "Plastron en diamant")
                .setLore("§7Equipement automatique à l'achat !", "", "§7§oCet item tombe au sol si", "§7§ovous mourez.", "", "§7Prix: §e500g", bank.getBalance() >= 500 ? "§eClic gauche pour acheter." : "§cVous n'avez pas assez de gold !")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();

        this.setItem(14, item, event -> {

            owner.closeInventory();

            if (bank.getBalance() >= 500) {

                if (owner.getInventory().getChestplate() == null || owner.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE) {

                    owner.sendMessage("§a§lACHAT! §ePlastron en Diamant");
                    bank.withdraw(500);
                    owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                    owner.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setUnbreakable(true).build());

                } else {

                    if (owner.getInventory().firstEmpty() != -1) {

                        owner.sendMessage("§a§lACHAT! §ePlastron en Diamant");
                        bank.withdraw(500);
                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                        owner.getInventory().addItem(owner.getInventory().getChestplate());
                        owner.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setUnbreakable(true).build());

                    } else {

                        owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                        owner.sendMessage("§cVotre inventaire est plein !");

                    }

                }

            } else {

                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                owner.sendMessage("§cVous n'avez pas assez de gold !");

            }
        });

        item = new ItemBuilder(Material.DIAMOND_BOOTS)
                .setName(ChatColor.YELLOW + "Bottes en diamant")
                .setLore("§7Equipement automatique à l'achat !", "", "§7§oCet item tombe au sol si", "§7§ovous mourez.", "", "§7Prix: §e300g", bank.getBalance() >= 300 ? "§eClic gauche pour acheter." : "§cVous n'avez pas assez de gold !")
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();

        this.setItem(15, item, event -> {

            owner.closeInventory();

            if (bank.getBalance() >= 300) {

                if (owner.getInventory().getBoots() == null || owner.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS) {

                    owner.sendMessage("§a§lACHAT! §eBottes en Diamant");
                    bank.withdraw(300);
                    owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                    owner.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable(true).build());

                } else {

                    if (owner.getInventory().firstEmpty() != -1) {

                        owner.sendMessage("§a§lACHAT! §eBottes en Diamant");
                        bank.withdraw(300);
                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                        owner.getInventory().addItem(owner.getInventory().getBoots());
                        owner.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable(true).build());

                    } else {

                        owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                        owner.sendMessage("§cVotre inventaire est plein !");

                    }

                }

            } else {

                owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                owner.sendMessage("§cVous n'avez pas assez de gold !");

            }
        });

    }
}
