package gg.warcraft.legends;

import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.legends.player.LegendsPlayerService;
import gg.warcraft.monolith.item.ItemService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitDistributor implements CommandExecutor {

    private final LegendsPlayerService playerService;

    public KitDistributor() {
        this(LegendsPlayerService.getInstance());
    }

    public KitDistributor(LegendsPlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        LegendsPlayer player = playerService.getLegendsPlayer(((Player) sender).getUniqueId());
        if (player == null) {
            return false;
        }

        if (player.getSpecialization() == null) {
            player.sendMessage(ChatColor.YELLOW + "You need to choose a specialization before you can grab a basic kit. Try /class.");
            return true;
        }

        switch (player.getSpecialization()) {
            case BERSERKER: {
                ItemStack axe = new ItemStack(Material.STONE_AXE);
                ItemMeta meta = axe.getItemMeta();
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Equipment",
                        "",
                        ChatColor.GRAY + "Strength +2"
                ));
                meta.setDisplayName("Basic Axe");
                meta.addItemFlags(ItemFlag.values());
                axe.setItemMeta(meta);
                player.giveItem(axe, false);

                for (ItemStack item : createBasicArmor("Strength")) {
                    player.giveItem(item, false);
                }
            }
            break;
            case ARMSMASTER: {
                ItemStack sword = new ItemStack(Material.STONE_SWORD);
                ItemMeta meta = sword.getItemMeta();
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Equipment",
                        "",
                        ChatColor.GRAY + "Strength +2"
                ));
                meta.setDisplayName("Basic Sword");
                meta.addItemFlags(ItemFlag.values());
                sword.setItemMeta(meta);
                player.giveItem(sword, false);

                for (ItemStack item : createBasicArmor("Strength")) {
                    player.giveItem(item, false);
                }
            }
            break;
            case HUNTER:
            case MARKSMAN: {
                ItemStack bow = new ItemStack(Material.BOW);
                ItemMeta meta = bow.getItemMeta();
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Equipment",
                        "",
                        ChatColor.GRAY + "Dexterity +2"
                ));
                meta.setDisplayName("Basic Bow");
                meta.addItemFlags(ItemFlag.values());
                bow.setItemMeta(meta);
                player.giveItem(bow, false);
                ItemStack arrow = new ItemStack(Material.ARROW);
                if (!player.getInventory().containsAtLeast(arrow, 1)) {
                    player.giveItem(arrow, false);
                }

                for (ItemStack item : createBasicArmor("Dexterity")) {
                    player.giveItem(item, false);
                }
            }
            break;
            case TEMPEST: {
                ItemStack staff = new ItemStack(Material.STONE_HOE);
                ItemMeta meta = staff.getItemMeta();
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Equipment",
                        "",
                        ChatColor.GRAY + "Intelligence +2"
                ));
                meta.setDisplayName("Basic Staff");
                meta.addItemFlags(ItemFlag.values());
                staff.setItemMeta(meta);
                player.giveItem(staff, false);

                for (ItemStack item : createBasicArmor("Intelligence")) {
                    player.giveItem(item, false);
                }
            }
            break;
            case WIZARD: {
                ItemStack wand = new ItemStack(Material.STICK);
                ItemMeta meta = wand.getItemMeta();
                meta.setLore(Arrays.asList(
                        ChatColor.GRAY + "Equipment",
                        "",
                        ChatColor.GRAY + "Intelligence +2"
                ));
                meta.setDisplayName("Basic Wand");
                meta.addItemFlags(ItemFlag.values());
                wand.setItemMeta(meta);
                player.giveItem(wand, false);

                for (ItemStack item : createBasicArmor("Intelligence")) {
                    player.giveItem(item, false);
                }
            }
            break;
        }
        return true;
    }

    private List<ItemStack> createBasicArmor(String stat) {
        List<ItemStack> items = new ArrayList<>();

        ItemStack helmet = ItemService.getInstance().newWrapper(new ItemStack(Material.CHAINMAIL_HELMET)).removeGenericArmor();
        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Equipment",
                "",
                ChatColor.GRAY + stat + " +2",
                ChatColor.GRAY + "Vitality +2"
        ));
        helmetMeta.setDisplayName("Basic Helmet");
        helmetMeta.addItemFlags(ItemFlag.values());
        helmet.setItemMeta(helmetMeta);
        items.add(helmet);

        ItemStack chest = ItemService.getInstance().newWrapper(new ItemStack(Material.CHAINMAIL_CHESTPLATE)).removeGenericArmor();
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Equipment",
                "",
                ChatColor.GRAY + stat + " +2",
                ChatColor.GRAY + "Vitality +2"
        ));
        chestMeta.setDisplayName("Basic Chestplate");
        chestMeta.addItemFlags(ItemFlag.values());
        chest.setItemMeta(chestMeta);
        items.add(chest);

        ItemStack leggings = ItemService.getInstance().newWrapper(new ItemStack(Material.CHAINMAIL_LEGGINGS)).removeGenericArmor();
        ItemMeta leggingsMeta = leggings.getItemMeta();
        leggingsMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Equipment",
                "",
                ChatColor.GRAY + stat + " +2",
                ChatColor.GRAY + "Vitality +2"
        ));
        leggingsMeta.setDisplayName("Basic Leggings");
        leggingsMeta.addItemFlags(ItemFlag.values());
        leggings.setItemMeta(leggingsMeta);
        items.add(leggings);

        ItemStack boots = ItemService.getInstance().newWrapper(new ItemStack(Material.CHAINMAIL_BOOTS)).removeGenericArmor();
        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Equipment",
                "",
                ChatColor.GRAY + stat + " +2",
                ChatColor.GRAY + "Vitality +2"
        ));
        bootsMeta.setDisplayName("Basic Boots");
        bootsMeta.addItemFlags(ItemFlag.values());
        boots.setItemMeta(bootsMeta);
        items.add(boots);

        return items;
    }
}
