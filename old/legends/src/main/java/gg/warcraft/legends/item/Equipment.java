package gg.warcraft.legends.item;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.player.LegendsPlayer;
import gg.warcraft.monolith.entity.Attribute;
import gg.warcraft.monolith.item.ItemService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static gg.warcraft.legends.entity.LegendsAttribute.STRENGTH;

public enum Equipment {
    // armor
    HEAD_OFFENSE_PRIM(Material.LEATHER_HELMET, null, LegendsAttribute.CRIT),
    HEAD_OFFENSE_SEC(Material.LEATHER_HELMET, null, LegendsAttribute.CRIT),
    HEAD_BALANCE_CRIT(Material.CHAINMAIL_HELMET, null, LegendsAttribute.CRIT),
    HEAD_BALANCE_VIT(Material.CHAINMAIL_HELMET, null, LegendsAttribute.VITALITY),
    HEAD_DEFENSE_PRIM(Material.IRON_HELMET, null, LegendsAttribute.VITALITY),
    HEAD_DEFENSE_SEC(Material.IRON_HELMET, null, LegendsAttribute.VITALITY),

    CHEST_OFFENSE_PRIM(Material.LEATHER_CHESTPLATE, null, LegendsAttribute.CRIT),
    CHEST_OFFENSE_SEC(Material.LEATHER_CHESTPLATE, null, LegendsAttribute.CRIT),
    CHEST_BALANCE_CRIT(Material.CHAINMAIL_CHESTPLATE, null, LegendsAttribute.CRIT),
    CHEST_BALANCE_VIT(Material.CHAINMAIL_CHESTPLATE, null, LegendsAttribute.VITALITY),
    CHEST_DEFENSE_PRIM(Material.IRON_CHESTPLATE, null, LegendsAttribute.VITALITY),
    CHEST_DEFENSE_SEC(Material.IRON_CHESTPLATE, null, LegendsAttribute.VITALITY),

    LEGS_OFFENSE_PRIM(Material.LEATHER_LEGGINGS, null, LegendsAttribute.CRIT),
    LEGS_OFFENSE_SEC(Material.LEATHER_LEGGINGS, null, LegendsAttribute.CRIT),
    LEGS_BALANCE_CRIT(Material.CHAINMAIL_LEGGINGS, null, LegendsAttribute.CRIT),
    LEGS_BALANCE_VIT(Material.CHAINMAIL_LEGGINGS, null, LegendsAttribute.VITALITY),
    LEGS_DEFENSE_PRIM(Material.IRON_LEGGINGS, null, LegendsAttribute.VITALITY),
    LEGS_DEFENSE_SEC(Material.IRON_LEGGINGS, null, LegendsAttribute.VITALITY),

    FEET_OFFENSE_PRIM(Material.LEATHER_BOOTS, null, LegendsAttribute.CRIT),
    FEET_OFFENSE_SEC(Material.LEATHER_BOOTS, null, LegendsAttribute.CRIT),
    FEET_BALANCE_CRIT(Material.CHAINMAIL_BOOTS, null, LegendsAttribute.CRIT),
    FEET_BALANCE_VIT(Material.CHAINMAIL_BOOTS, null, LegendsAttribute.VITALITY),
    FEET_DEFENSE_PRIM(Material.IRON_BOOTS, null, LegendsAttribute.VITALITY),
    FEET_DEFENSE_SEC(Material.IRON_BOOTS, null, LegendsAttribute.VITALITY),

    // main hand
    SWORD_OFFENSE_PRIM(Material.WOOD_SWORD, STRENGTH, LegendsAttribute.CRIT),
    SWORD_OFFENSE_SEC(Material.WOOD_SWORD, STRENGTH, LegendsAttribute.CRIT),
    SWORD_BALANCE_CRIT(Material.STONE_SWORD, STRENGTH, LegendsAttribute.CRIT),
    SWORD_BALANCE_VIT(Material.STONE_SWORD, STRENGTH, LegendsAttribute.VITALITY),
    SWORD_DEFENSE_PRIM(Material.IRON_SWORD, STRENGTH, LegendsAttribute.VITALITY),
    SWORD_DEFENSE_SEC(Material.IRON_SWORD, STRENGTH, LegendsAttribute.VITALITY),

    AXE_OFFENSE_PRIM(Material.WOOD_AXE, STRENGTH, LegendsAttribute.CRIT),
    AXE_OFFENSE_SEC(Material.WOOD_AXE, STRENGTH, LegendsAttribute.CRIT),
    AXE_BALANCE_CRIT(Material.STONE_AXE, STRENGTH, LegendsAttribute.CRIT),
    AXE_BALANCE_VIT(Material.STONE_AXE, STRENGTH, LegendsAttribute.VITALITY),
    AXE_DEFENSE_PRIM(Material.IRON_AXE, STRENGTH, LegendsAttribute.VITALITY),
    AXE_DEFENSE_SEC(Material.IRON_AXE, STRENGTH, LegendsAttribute.VITALITY),

    BOW_OFFENSE_PRIM(Material.BOW, LegendsAttribute.DEXTERITY, LegendsAttribute.CRIT),
    BOW_OFFENSE_SEC(Material.BOW, LegendsAttribute.DEXTERITY, LegendsAttribute.CRIT),
    BOW_BALANCE_CRIT(Material.BOW, LegendsAttribute.DEXTERITY, LegendsAttribute.CRIT),
    BOW_BALANCE_VIT(Material.BOW, LegendsAttribute.DEXTERITY, LegendsAttribute.VITALITY),
    BOW_DEFENSE_PRIM(Material.BOW, LegendsAttribute.DEXTERITY, LegendsAttribute.VITALITY),
    BOW_DEFENSE_SEC(Material.BOW, LegendsAttribute.DEXTERITY, LegendsAttribute.VITALITY),

    WAND_OFFENSE_PRIM(Material.STICK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    WAND_OFFENSE_SEC(Material.STICK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    WAND_BALANCE_CRIT(Material.STICK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    WAND_BALANCE_VIT(Material.STICK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),
    WAND_DEFENSE_PRIM(Material.STICK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),
    WAND_DEFENSE_SEC(Material.STICK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),

    STAFF_OFFENSE_PRIM(Material.WOOD_HOE, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    STAFF_OFFENSE_SEC(Material.WOOD_HOE, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    STAFF_BALANCE_CRIT(Material.STONE_HOE, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    STAFF_BALANCE_VIT(Material.STONE_HOE, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),
    STAFF_DEFENSE_PRIM(Material.IRON_HOE, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),
    STAFF_DEFENSE_SEC(Material.IRON_HOE, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),

    // off hand
    OFFHAND_SHIELD_OFFENSE_PRIM(Material.SHIELD, LegendsAttribute.STRENGTH, null),
    OFFHAND_SHIELD_OFFENSE_CRIT(Material.SHIELD, LegendsAttribute.CRIT, null),
    OFFHAND_SHIELD_BALANCE_CRIT(Material.SHIELD, LegendsAttribute.STRENGTH, LegendsAttribute.CRIT),
    OFFHAND_SHIELD_BALANCE_VIT(Material.SHIELD, LegendsAttribute.STRENGTH, LegendsAttribute.VITALITY),
    OFFHAND_SHIELD_DEFENSE(Material.SHIELD, LegendsAttribute.VITALITY, null),

    OFFHAND_BOOK_OFFENSE_PRIM(Material.BOOK, LegendsAttribute.INTELLIGENCE, null),
    OFFHAND_BOOK_OFFENSE_CRIT(Material.BOOK, LegendsAttribute.CRIT, null),
    OFFHAND_BOOK_BALANCE_CRIT(Material.BOOK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.CRIT),
    OFFHAND_BOOK_BALANCE_VIT(Material.BOOK, LegendsAttribute.INTELLIGENCE, LegendsAttribute.VITALITY),
    OFFHAND_BOOK_DEFENSE(Material.BOOK, LegendsAttribute.VITALITY, null),

    OFFHAND_SKULL_OFFENSE_PRIM(Material.SKULL_ITEM, LegendsAttribute.STRENGTH, null),
    OFFHAND_SKULL_OFFENSE_CRIT(Material.SKULL_ITEM, LegendsAttribute.CRIT, null),
    OFFHAND_SKULL_BALANCE_CRIT(Material.SKULL_ITEM, LegendsAttribute.STRENGTH, LegendsAttribute.CRIT),
    OFFHAND_SKULL_BALANCE_VIT(Material.SKULL_ITEM, LegendsAttribute.STRENGTH, LegendsAttribute.VITALITY),
    OFFHAND_SKULL_DEFENSE(Material.SKULL_ITEM, LegendsAttribute.VITALITY, null);

    private static final Random random = new Random(System.currentTimeMillis());
    private static final ItemNames itemNames = new ItemNames();

    private final Material defaultMaterial;
    private final Attribute primaryStat;
    private final Attribute secondaryStat;

    Equipment(Material defaultMaterial, Attribute primaryStat, Attribute secondaryStat) {
        this.defaultMaterial = defaultMaterial;
        this.primaryStat = primaryStat;
        this.secondaryStat = secondaryStat;
    }

    private static boolean isDiamond = false;

    private Material getUpgradedMaterial(Material type, boolean cheat) {
        double requiredChance;
        if (type == Material.SHIELD || type == Material.BOOK || type == Material.SKULL_ITEM) { // is offhand
            requiredChance = 0.95;
        } else {
            requiredChance = 0.85;
        }
        if (random.nextDouble() >= requiredChance || cheat) {
            isDiamond = true;
            if (type == Material.STICK) {
                return Material.BLAZE_ROD;
            }
            String newType = type.name()
                    .replace("WOOD", "DIAMOND")
                    .replace("IRON", "DIAMOND")
                    .replace("STONE", "DIAMOND")
                    .replace("LEATHER", "DIAMOND")
                    .replace("CHAINMAIL", "DIAMOND");
            return Material.valueOf(newType);
        } else {
            String newType = type.name()
                    .replace("WOOD", "GOLD")
                    .replace("IRON", "GOLD")
                    .replace("STONE", "GOLD")
                    .replace("LEATHER", "GOLD")
                    .replace("CHAINMAIL", "GOLD");
            return Material.valueOf(newType);
        }
    }

    private String getTypeName(Material material, Attribute primaryStat) {
        switch (primaryStat.getName()) {
            case "Strength":
                return itemNames.getStrengthType(material);
            case "Intelligence":
                return itemNames.getIntellectType(material);
            case "Dexterity":
                return itemNames.getDexterityType(material);
            default:
                return "Thing";
        }
    }

    public ItemStack generateRandom(LegendsPlayer player, Attribute primaryStatOverride, boolean cheat) {
        Attribute primaryStat = this.primaryStat != null ? this.primaryStat : primaryStatOverride;
        Material materialType = defaultMaterial;
        int primaryStatRoll = 0;
        int secondaryStatRoll = 0;
        String itemColor = "";
        String itemName = "";

        if (name().contains("OFFHAND")) {
            if (name().contains("BALANCE")) {
                primaryStatRoll = getOffhandBalanceRoll();
                secondaryStatRoll = getOffhandBalanceRoll();
                if ((primaryStatRoll == 2 && secondaryStatRoll == 2) || cheat) {
                    materialType = getUpgradedMaterial(defaultMaterial, cheat);
                    if (isDiamond) {
                        isDiamond = false;
                        primaryStatRoll = 3;
                        secondaryStatRoll = 3;
                        itemColor = ChatColor.AQUA.toString();
                        itemName = itemNames.getLegendaryOffhandName(player.getRace(), "BALANCE", materialType);
                    } else {
                        itemColor = ChatColor.YELLOW.toString();
                    }
                }
                if (itemName.isEmpty()) {
                    itemName = itemNames.getBalancePrefix(player.getRace(), primaryStat) + " " +
                            getTypeName(materialType, primaryStat);
                }
            } else {
                primaryStatRoll = getOffhandRoll();
                if (primaryStatRoll == 3 || cheat) {
                    materialType = getUpgradedMaterial(defaultMaterial, cheat);
                    if (isDiamond) {
                        isDiamond = false;
                        primaryStatRoll = 5;
                        itemColor = ChatColor.AQUA.toString();
                        String rollType = name().contains("OFFENSE") ? "OFFENSE" : "DEFENSE";
                        itemName = itemNames.getLegendaryOffhandName(player.getRace(), rollType, materialType);
                    } else {
                        itemColor = ChatColor.YELLOW.toString();
                    }
                }
                if (itemName.isEmpty()) {
                    Attribute spoofedPrimaryStat = primaryStat;
                    if (name().contains("SKULL") || name().contains("SHIELD")) {
                        spoofedPrimaryStat = LegendsAttribute.STRENGTH;
                    }
                    if (name().contains("BOOK")) {
                        spoofedPrimaryStat = LegendsAttribute.INTELLIGENCE;
                    }
                    itemName = name().contains("OFFENSE")
                            ? itemNames.getOffensePrefix(player.getRace(), spoofedPrimaryStat) + " " +
                            getTypeName(materialType, spoofedPrimaryStat)
                            : itemNames.getDefensePrefix(player.getRace(), spoofedPrimaryStat) + " " +
                            getTypeName(materialType, spoofedPrimaryStat);
                }
            }
        } else {
            if (name().contains("BALANCE")) {
                primaryStatRoll = getMedRoll();
                secondaryStatRoll = getMedRoll();
                if ((primaryStatRoll == 7 && secondaryStatRoll == 7) || cheat) {
                    materialType = getUpgradedMaterial(defaultMaterial, cheat);
                    if (isDiamond) {
                        isDiamond = false;
                        primaryStatRoll = 9;
                        secondaryStatRoll = 9;
                        itemColor = ChatColor.AQUA.toString();
                        itemName = itemNames.getLegendaryBalanceName(player.getRace(), primaryStat, materialType);
                    } else {
                        itemColor = ChatColor.YELLOW.toString();
                    }
                }
                if (itemName.isEmpty()) {
                    itemName = itemNames.getBalancePrefix(player.getRace(), primaryStat) + " " +
                            getTypeName(materialType, primaryStat);
                }
            } else if (name().contains("PRIM")) {
                primaryStatRoll = getHighRoll();
                secondaryStatRoll = getLowRoll();
                if ((primaryStatRoll == 10 && secondaryStatRoll == 5) || cheat) {
                    materialType = getUpgradedMaterial(defaultMaterial, cheat);
                    if (isDiamond) {
                        isDiamond = false;
                        primaryStatRoll = 12;
                        secondaryStatRoll = 7;
                        itemColor = ChatColor.AQUA.toString();
                        if (secondaryStat == LegendsAttribute.CRIT) {
                            itemName = itemNames.getLegendaryOffenseName(player.getRace(), primaryStat, materialType);
                        } else {
                            itemName = itemNames.getLegendaryDefenseName(player.getRace(), primaryStat, materialType);
                        }
                    } else {
                        itemColor = ChatColor.YELLOW.toString();
                    }
                }
                if (itemName.isEmpty()) {
                    if (secondaryStat == LegendsAttribute.CRIT) {
                        itemName = itemNames.getOffensePrefix(player.getRace(), primaryStat) + " " +
                                getTypeName(materialType, primaryStat);
                    } else {
                        itemName = itemNames.getDefensePrefix(player.getRace(), primaryStat) + " " +
                                getTypeName(materialType, primaryStat);
                    }
                }
            } else if (name().contains("SEC")) {
                primaryStatRoll = getLowRoll();
                secondaryStatRoll = getHighRoll();
                if ((primaryStatRoll == 5 && secondaryStatRoll == 10) || cheat) {
                    materialType = getUpgradedMaterial(defaultMaterial, cheat);
                    if (isDiamond) {
                        isDiamond = false;
                        primaryStatRoll = 7;
                        secondaryStatRoll = 12;
                        itemColor = ChatColor.AQUA.toString();
                        if (secondaryStat == LegendsAttribute.CRIT) {
                            itemName = itemNames.getLegendaryOffenseName(player.getRace(), primaryStat, materialType);
                        } else {
                            itemName = itemNames.getLegendaryDefenseName(player.getRace(), primaryStat, materialType);
                        }
                    } else {
                        itemColor = ChatColor.YELLOW.toString();
                    }
                }
                if (itemName.isEmpty()) {
                    if (secondaryStat == LegendsAttribute.CRIT) {
                        itemName = itemNames.getOffensePrefix(player.getRace(), primaryStat) + " " +
                                getTypeName(materialType, primaryStat);
                    } else {
                        itemName = itemNames.getDefensePrefix(player.getRace(), primaryStat) + " " +
                                getTypeName(materialType, primaryStat);
                    }
                }
            } else {
                System.out.println("COULD NOT FIND ITEM TYPE AT THE BOTTOM OF EQUIPMENT ENUM");
            }
        }

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Equipment");
        lore.add("");
        lore.add(ChatColor.GRAY + primaryStat.getName() + " +" + primaryStatRoll);
        if (secondaryStat != null) {
            lore.add(ChatColor.GRAY + secondaryStat.getName() + " +" + secondaryStatRoll);
        }
        lore.addAll(itemNames.getFlavorText(itemName));

        ItemStack item = ItemService.getInstance().newWrapper(new ItemStack(materialType)).removeGenericArmor();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(itemColor + itemName);
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);

        return item;
    }

    private int getHighRoll() {
        double roll = random.nextDouble();
        if (roll >= 0.98) {
            return 10;
        } else if (roll >= 0.93) {
            return 9;
        } else if (roll >= 0.78) {
            return 8;
        } else if (roll >= 0.58) {
            return 7;
        } else if (roll >= 0.33) {
            return 6;
        } else {
            return 5;
        }
    }

    private int getMedRoll() {
        return getLowRoll() + 2;
    }

    private int getLowRoll() {
        double roll = random.nextDouble();
        if (roll >= 0.95) {
            return 5;
        } else if (roll >= 0.85) {
            return 4;
        } else if (roll >= 0.60) {
            return 3;
        } else if (roll >= 0.35) {
            return 2;
        } else {
            return 1;
        }
    }

    private int getOffhandRoll() {
        double roll = random.nextDouble();
        if (roll >= 0.90) {
            return 3;
        } else if (roll >= 0.55) {
            return 2;
        } else {
            return 1;
        }
    }

    private int getOffhandBalanceRoll() {
        double roll = random.nextDouble();
        if (roll >= 0.80) {
            return 2;
        } else {
            return 1;
        }
    }
}
