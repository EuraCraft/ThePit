package fr.maximouz.thepit.inventories;

import fr.euracraft.api.gui.AbstractInterface;
import fr.euracraft.api.item.ItemBuilder;
import fr.maximouz.thepit.bank.Bank;
import fr.maximouz.thepit.items.AntiBountiedItem;
import fr.maximouz.thepit.items.TemporaryObsidianItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ShopInventory extends AbstractInterface {

    public ShopInventory(Player owner, Bank bank) {
        super(owner, ChatColor.DARK_GRAY + "Items temporaires", 3*9);

        this.update(() -> {

            ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD)
                    .setName(ChatColor.YELLOW + "Épée en Diamant")
                    .setLore(ChatColor.BLUE + "+20% de dégâts contre les joueurs",
                            ChatColor.BLUE + "ayant une prime.",
                            "",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "Cet item tombe au sol",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "si vous mourez.",
                            "",
                            ChatColor.GRAY + "Prix: " + ChatColor.YELLOW + "150g",
                            bank.getBalance() >= 150 ? ChatColor.YELLOW + "Clic gauche pour acheter." : ChatColor.RED + "Vous n'avez pas assez de gold !"
                    ).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build();

            this.setItem(11, item, event -> {

                double price = 150.0;

                if (bank.getBalance() >= price) {

                    if (owner.getInventory().firstEmpty() != -1 || owner.getInventory().contains(Material.IRON_SWORD)) {

                        owner.sendMessage("§a§lACHAT! §eÉpée en Diamant");
                        bank.withdraw(price);
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

            item = new ItemBuilder(Material.OBSIDIAN, 8)
                    .setName(ChatColor.YELLOW + "Obsidienne")
                    .setLore(ChatColor.GRAY + "Disparait au bout de 2 minutes",
                            "",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "Cet item tombe au sol si",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "vous mourez.",
                            "",
                            ChatColor.GRAY + "Prix: " + ChatColor.YELLOW + "50g",
                            bank.getBalance() >= 50 ? ChatColor.YELLOW + "Clic gauche pour acheter." : ChatColor.RED + "Vous n'avez pas assez de gold !"
                    ).build();

            this.setItem(12, item, event -> {

                double price = 50.0;

                if (bank.getBalance() >= price) {

                    if (owner.getInventory().firstEmpty() != -1) {

                        bank.withdraw(price);
                        owner.getInventory().addItem(new TemporaryObsidianItem(8).build());
                        owner.playSound(owner.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                        owner.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "ACHAT! " + ChatColor.YELLOW + "8 Obsidiennes");

                    } else {

                        owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                        owner.sendMessage("§Votre inventaire est plein !");

                    }

                } else {

                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    owner.sendMessage("§cVous n'avez pas assez de gold !");

                }

            });

            item = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                    .setName(ChatColor.YELLOW + "Plastron en diamant")
                    .setLore(ChatColor.GRAY + "Equipement automatique à l'achat !",
                            "",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "Cet item tombe au sol si",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "vous mourez.",
                            "",
                            ChatColor.GRAY + "Prix: " + ChatColor.YELLOW + "500g",
                            bank.getBalance() >= 500 ? ChatColor.YELLOW + "Clic gauche pour acheter." : ChatColor.RED + "Vous n'avez pas assez de gold !"
                    ).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build();

            this.setItem(14, item, event -> {

                double price = 500;

                if (bank.getBalance() >= price) {

                    if (owner.getInventory().firstEmpty() != -1) {

                        bank.withdraw(price);
                        owner.sendMessage("§a§lACHAT! §ePlastron en Diamant");
                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                        if (owner.getInventory().getChestplate() != null) {
                            ItemStack[] inventory = owner.getInventory().getContents();
                            int index = -1;
                            for(int i = 9; i < inventory.length; i++) {
                                if (inventory[i] == null) {
                                    index = i;
                                    inventory[i] = owner.getInventory().getChestplate();
                                    owner.getInventory().setContents(inventory);
                                    break;
                                }
                            }
                            if (index == -1)
                                owner.getInventory().addItem(owner.getInventory().getChestplate());
                        }

                        owner.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setUnbreakable(true).build());

                    } else {

                        owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                        owner.sendMessage("§cVotre inventaire est plein !");

                    }

                } else {

                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    owner.sendMessage("§cVous n'avez pas assez de gold !");

                }
            });

            item = new ItemBuilder(Material.DIAMOND_BOOTS)
                    .setName(ChatColor.YELLOW + "Bottes en diamant")
                    .setLore(ChatColor.GRAY + "Equipement automatique à l'achat !",
                            "",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "Cet item tombe au sol si",
                            ChatColor.GRAY + "" + ChatColor.ITALIC + "vous mourez.",
                            "",
                            ChatColor.GRAY + "Prix: " + ChatColor.YELLOW + "300g",
                            bank.getBalance() >= 300 ? ChatColor.YELLOW + "Clic gauche pour acheter." : ChatColor.RED + "Vous n'avez pas assez de gold !"
                    ).addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build();

            this.setItem(15, item, event -> {

                if (bank.getBalance() >= 300) {

                    if (owner.getInventory().firstEmpty() != -1) {

                        owner.sendMessage("§a§lACHAT! §eBottes en Diamant");
                        bank.withdraw(300);
                        owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1f, 1f);

                        if (owner.getInventory().getBoots() != null) {
                            ItemStack[] inventory = owner.getInventory().getContents();
                            int index = -1;
                            for(int i = 9; i < inventory.length; i++) {
                                if (inventory[i] == null) {
                                    index = i;
                                    inventory[i] = owner.getInventory().getBoots();
                                    owner.getInventory().setContents(inventory);
                                    break;
                                }
                            }
                            if (index == -1)
                                owner.getInventory().addItem(owner.getInventory().getBoots());
                        }

                        owner.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).setUnbreakable(true).build());

                    } else {

                        owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                        owner.sendMessage("§cVotre inventaire est plein !");

                    }

                } else {

                    owner.playSound(owner.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                    owner.sendMessage("§cVous n'avez pas assez de gold !");

                }
            });

        }, 0);

    }
}
