package gg.warcraft.legends.item;

import gg.warcraft.legends.entity.LegendsAttribute;
import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.monolith.entity.Attribute;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static gg.warcraft.legends.entity.LegendsAttribute.STRENGTH;

public class ItemNames {
    private static final Random random = new Random(System.currentTimeMillis());
    private static final String UNIDENTIFIED = "Unidentified";
    private static final Map<Attribute, String> humanOffensePrefixes;
    private static final Map<Attribute, String> humanBalancePrefixes;
    private static final Map<Attribute, String> humanDefensePrefixes;
    private static final Map<Attribute, String> orcOffensePrefixes;
    private static final Map<Attribute, String> orcBalancePrefixes;
    private static final Map<Attribute, String> orcDefensePrefixes;
    private static final Map<Material, List<String>> humanOffenseLegendaryStrengthNames;
    private static final Map<Material, List<String>> humanOffenseLegendaryDexterityNames;
    private static final Map<Material, List<String>> humanOffenseLegendaryIntellectNames;
    private static final Map<Material, List<String>> humanBalanceLegendaryStrengthNames;
    private static final Map<Material, List<String>> humanBalanceLegendaryDexterityNames;
    private static final Map<Material, List<String>> humanBalanceLegendaryIntellectNames;
    private static final Map<Material, List<String>> humanDefenseLegendaryStrengthNames;
    private static final Map<Material, List<String>> humanDefenseLegendaryDexterityNames;
    private static final Map<Material, List<String>> humanDefenseLegendaryIntellectNames;
    private static final Map<Material, List<String>> humanOffenseLegendaryOffhandNames;
    private static final Map<Material, List<String>> humanBalanceLegendaryOffhandNames;
    private static final Map<Material, List<String>> humanDefenseLegendaryOffhandNames;
    private static final Map<Material, List<String>> orcOffenseLegendaryStrengthNames;
    private static final Map<Material, List<String>> orcOffenseLegendaryDexterityNames;
    private static final Map<Material, List<String>> orcOffenseLegendaryIntellectNames;
    private static final Map<Material, List<String>> orcBalanceLegendaryStrengthNames;
    private static final Map<Material, List<String>> orcBalanceLegendaryDexterityNames;
    private static final Map<Material, List<String>> orcBalanceLegendaryIntellectNames;
    private static final Map<Material, List<String>> orcDefenseLegendaryStrengthNames;
    private static final Map<Material, List<String>> orcDefenseLegendaryDexterityNames;
    private static final Map<Material, List<String>> orcDefenseLegendaryIntellectNames;
    private static final Map<Material, List<String>> orcOffenseLegendaryOffhandNames;
    private static final Map<Material, List<String>> orcBalanceLegendaryOffhandNames;
    private static final Map<Material, List<String>> orcDefenseLegendaryOffhandNames;

    static {
        humanOffensePrefixes = new HashMap<>();
        humanOffensePrefixes.put(STRENGTH, "Hero");
        humanOffensePrefixes.put(LegendsAttribute.DEXTERITY, "Cutthroat");
        humanOffensePrefixes.put(LegendsAttribute.INTELLIGENCE, "Witch");

        humanBalancePrefixes = new HashMap<>();
        humanBalancePrefixes.put(STRENGTH, "Champion");
        humanBalancePrefixes.put(LegendsAttribute.DEXTERITY, "Swindler");
        humanBalancePrefixes.put(LegendsAttribute.INTELLIGENCE, "Sorcerer");

        humanDefensePrefixes = new HashMap<>();
        humanDefensePrefixes.put(STRENGTH, "Guardian");
        humanDefensePrefixes.put(LegendsAttribute.DEXTERITY, "Envoy");
        humanDefensePrefixes.put(LegendsAttribute.INTELLIGENCE, "Enchanter");

        orcOffensePrefixes = new HashMap<>();
        orcOffensePrefixes.put(STRENGTH, "Warmonger");
        orcOffensePrefixes.put(LegendsAttribute.DEXTERITY, "Huntmaster");
        orcOffensePrefixes.put(LegendsAttribute.INTELLIGENCE, "Warlock");

        orcBalancePrefixes = new HashMap<>();
        orcBalancePrefixes.put(STRENGTH, "Chieftain");
        orcBalancePrefixes.put(LegendsAttribute.DEXTERITY, "Beastwrangler");
        orcBalancePrefixes.put(LegendsAttribute.INTELLIGENCE, "Seer");

        orcDefensePrefixes = new HashMap<>();
        orcDefensePrefixes.put(STRENGTH, "Rampart");
        orcDefensePrefixes.put(LegendsAttribute.DEXTERITY, "Mudlurker");
        orcDefensePrefixes.put(LegendsAttribute.INTELLIGENCE, "Shaman");

        humanOffenseLegendaryStrengthNames = new HashMap<>();
        humanOffenseLegendaryStrengthNames.put(Material.DIAMOND_SWORD, Arrays.asList("Orcbane", "The Bloody Baron"));
        humanOffenseLegendaryStrengthNames.put(Material.DIAMOND_AXE, Arrays.asList("Jagerax"));
        humanOffenseLegendaryStrengthNames.put(Material.DIAMOND_HELMET, Arrays.asList("Conqueror Helmet"));
        humanOffenseLegendaryStrengthNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Conqueror Chestplate"));
        humanOffenseLegendaryStrengthNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Conqueror Legplates"));
        humanOffenseLegendaryStrengthNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Conqueror Greaves"));

        humanOffenseLegendaryDexterityNames = new HashMap<>();
        humanOffenseLegendaryDexterityNames.put(Material.BOW, Arrays.asList("Gaium Oak", "Elysium"));
        humanOffenseLegendaryDexterityNames.put(Material.DIAMOND_HELMET, Arrays.asList("Von Kleft's Cap"));
        humanOffenseLegendaryDexterityNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Von Kleft's Coat"));
        humanOffenseLegendaryDexterityNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Von Kleft's Leggings"));
        humanOffenseLegendaryDexterityNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Von Kleft's Boots"));

        humanOffenseLegendaryIntellectNames = new HashMap<>();
        humanOffenseLegendaryIntellectNames.put(Material.BLAZE_ROD, Arrays.asList("Hand of the Haruspex"));
        humanOffenseLegendaryIntellectNames.put(Material.DIAMOND_HOE, Arrays.asList("Grand Deceiver’s Rod"));
        humanOffenseLegendaryIntellectNames.put(Material.DIAMOND_HELMET, Arrays.asList("Twisted Hat"));
        humanOffenseLegendaryIntellectNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Twisted Robe"));
        humanOffenseLegendaryIntellectNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Twisted Pants"));
        humanOffenseLegendaryIntellectNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Twisted Shoes"));

        humanBalanceLegendaryStrengthNames = new HashMap<>();
        humanBalanceLegendaryStrengthNames.put(Material.DIAMOND_SWORD, Arrays.asList("Golden Rimmed Zweihander"));
        humanBalanceLegendaryStrengthNames.put(Material.DIAMOND_AXE, Arrays.asList("Great Estoc"));
        humanBalanceLegendaryStrengthNames.put(Material.DIAMOND_HELMET, Arrays.asList("Greatplate Helmet"));
        humanBalanceLegendaryStrengthNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Greatplate Chestplate"));
        humanBalanceLegendaryStrengthNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Greatplate Legplates"));
        humanBalanceLegendaryStrengthNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Greatplate Greaves"));

        humanBalanceLegendaryDexterityNames = new HashMap<>();
        humanBalanceLegendaryDexterityNames.put(Material.BOW, Arrays.asList("Truthseeker"));
        humanBalanceLegendaryDexterityNames.put(Material.DIAMOND_HELMET, Arrays.asList("Gloomwood Cap"));
        humanBalanceLegendaryDexterityNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Gloomwood Coat"));
        humanBalanceLegendaryDexterityNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Gloomwood Leggings"));
        humanBalanceLegendaryDexterityNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Gloomwood Boots"));

        humanBalanceLegendaryIntellectNames = new HashMap<>();
        humanBalanceLegendaryIntellectNames.put(Material.BLAZE_ROD, Arrays.asList("Bright"));
        humanBalanceLegendaryIntellectNames.put(Material.DIAMOND_HOE, Arrays.asList("Talking Turtle Taff"));
        humanBalanceLegendaryIntellectNames.put(Material.DIAMOND_HELMET, Arrays.asList("Arcane Hat"));
        humanBalanceLegendaryIntellectNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Arcane Robe"));
        humanBalanceLegendaryIntellectNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Arcane Pants"));
        humanBalanceLegendaryIntellectNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Arcane Shoes"));

        humanDefenseLegendaryStrengthNames = new HashMap<>();
        humanDefenseLegendaryStrengthNames.put(Material.DIAMOND_SWORD, Arrays.asList("Excalibur"));
        humanDefenseLegendaryStrengthNames.put(Material.DIAMOND_AXE, Arrays.asList("The Cleanser"));
        humanDefenseLegendaryStrengthNames.put(Material.DIAMOND_HELMET, Arrays.asList("Sovereign Helmet"));
        humanDefenseLegendaryStrengthNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Sovereign Chestplate"));
        humanDefenseLegendaryStrengthNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Sovereign Legplates"));
        humanDefenseLegendaryStrengthNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Sovereign Greaves"));

        humanDefenseLegendaryDexterityNames = new HashMap<>();
        humanDefenseLegendaryDexterityNames.put(Material.BOW, Arrays.asList("Skytouched Recurve"));
        humanDefenseLegendaryDexterityNames.put(Material.DIAMOND_HELMET, Arrays.asList("Bone Cap"));
        humanDefenseLegendaryDexterityNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Bone Coat"));
        humanDefenseLegendaryDexterityNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Bone Leggings"));
        humanDefenseLegendaryDexterityNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Bone Boots"));

        humanDefenseLegendaryIntellectNames = new HashMap<>();
        humanDefenseLegendaryIntellectNames.put(Material.BLAZE_ROD, Arrays.asList("Deflector"));
        humanDefenseLegendaryIntellectNames.put(Material.DIAMOND_HOE, Arrays.asList("Warpwood Cane"));
        humanDefenseLegendaryIntellectNames.put(Material.DIAMOND_HELMET, Arrays.asList("Luminous Velvet Hat"));
        humanDefenseLegendaryIntellectNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Luminous Velvet Robe"));
        humanDefenseLegendaryIntellectNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Luminous Velvet Pants"));
        humanDefenseLegendaryIntellectNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Luminous Velvet Shoes"));

        humanOffenseLegendaryOffhandNames = new HashMap<>();
        humanOffenseLegendaryOffhandNames.put(Material.BOOK, Arrays.asList("Notes of the Crone"));
        humanOffenseLegendaryOffhandNames.put(Material.SHIELD, Arrays.asList("Guardian of the West"));
        humanOffenseLegendaryOffhandNames.put(Material.SKULL_ITEM, Arrays.asList("Berserker's Skull"));

        humanBalanceLegendaryOffhandNames = new HashMap<>();
        humanBalanceLegendaryOffhandNames.put(Material.BOOK, Arrays.asList("The Great Mage's Journal"));
        humanBalanceLegendaryOffhandNames.put(Material.SHIELD, Arrays.asList("Battered Bastion"));
        humanBalanceLegendaryOffhandNames.put(Material.SKULL_ITEM, Arrays.asList("Berserker's Skull"));

        humanDefenseLegendaryOffhandNames = new HashMap<>();
        humanDefenseLegendaryOffhandNames.put(Material.BOOK, Arrays.asList("Withered Lexicon"));
        humanDefenseLegendaryOffhandNames.put(Material.SHIELD, Arrays.asList("Blinding Redoubt"));
        humanDefenseLegendaryOffhandNames.put(Material.SKULL_ITEM, Arrays.asList("Berserker's Skull"));

        orcOffenseLegendaryStrengthNames = new HashMap<>();
        orcOffenseLegendaryStrengthNames.put(Material.DIAMOND_SWORD, Arrays.asList("Executioner’s Blade"));
        orcOffenseLegendaryStrengthNames.put(Material.DIAMOND_AXE, Arrays.asList("Searing Cleaver", "Gutripper"));
        orcOffenseLegendaryStrengthNames.put(Material.DIAMOND_HELMET, Arrays.asList("Spiked Helmet"));
        orcOffenseLegendaryStrengthNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Spiked Chestplate"));
        orcOffenseLegendaryStrengthNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Spiked Legplates"));
        orcOffenseLegendaryStrengthNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Spiked Greaves"));

        orcOffenseLegendaryDexterityNames = new HashMap<>();
        orcOffenseLegendaryDexterityNames.put(Material.BOW, Arrays.asList("Eyegouger"));
        orcOffenseLegendaryDexterityNames.put(Material.DIAMOND_HELMET, Arrays.asList("The Bull's Cap"));
        orcOffenseLegendaryDexterityNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("The Bull's Coat"));
        orcOffenseLegendaryDexterityNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("The Bull's Leggings"));
        orcOffenseLegendaryDexterityNames.put(Material.DIAMOND_BOOTS, Arrays.asList("The Bull's Boots"));

        orcOffenseLegendaryIntellectNames = new HashMap<>();
        orcOffenseLegendaryIntellectNames.put(Material.BLAZE_ROD, Arrays.asList("Elder Spirit"));
        orcOffenseLegendaryIntellectNames.put(Material.DIAMOND_HOE, Arrays.asList("K'took the Serpent"));
        orcOffenseLegendaryIntellectNames.put(Material.DIAMOND_HELMET, Arrays.asList("Pox Ridden Hat"));
        orcOffenseLegendaryIntellectNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Pox Ridden Robe"));
        orcOffenseLegendaryIntellectNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Pox Ridden Pants"));
        orcOffenseLegendaryIntellectNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Pox Ridden Shoes"));

        orcBalanceLegendaryStrengthNames = new HashMap<>();
        orcBalanceLegendaryStrengthNames.put(Material.DIAMOND_SWORD, Arrays.asList("Armageddon"));
        orcBalanceLegendaryStrengthNames.put(Material.DIAMOND_AXE, Arrays.asList("Skullsplitter"));
        orcBalanceLegendaryStrengthNames.put(Material.DIAMOND_HELMET, Arrays.asList("Warchief Helmet"));
        orcBalanceLegendaryStrengthNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Warchief Chestplate"));
        orcBalanceLegendaryStrengthNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Warchief Legplates"));
        orcBalanceLegendaryStrengthNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Warchief Greaves"));

        orcBalanceLegendaryDexterityNames = new HashMap<>();
        orcBalanceLegendaryDexterityNames.put(Material.BOW, Arrays.asList("Gutgorger"));
        orcBalanceLegendaryDexterityNames.put(Material.DIAMOND_HELMET, Arrays.asList("Barazaak's Cap"));
        orcBalanceLegendaryDexterityNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Barazaak's Coat"));
        orcBalanceLegendaryDexterityNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Barazaak's Leggings"));
        orcBalanceLegendaryDexterityNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Barazaak's Boots"));

        orcBalanceLegendaryIntellectNames = new HashMap<>();
        orcBalanceLegendaryIntellectNames.put(Material.BLAZE_ROD, Arrays.asList("Baba's Fetish"));
        orcBalanceLegendaryIntellectNames.put(Material.DIAMOND_HOE, Arrays.asList("Tuskling Prodder"));
        orcBalanceLegendaryIntellectNames.put(Material.DIAMOND_HELMET, Arrays.asList("Ceremonial Hat"));
        orcBalanceLegendaryIntellectNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Ceremonial Robe"));
        orcBalanceLegendaryIntellectNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Ceremonial Pants"));
        orcBalanceLegendaryIntellectNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Ceremonial Shoes"));

        orcDefenseLegendaryStrengthNames = new HashMap<>();
        orcDefenseLegendaryStrengthNames.put(Material.DIAMOND_SWORD, Arrays.asList("Bull's Horn"));
        orcDefenseLegendaryStrengthNames.put(Material.DIAMOND_AXE, Arrays.asList("Warlord's Cleaver"));
        orcDefenseLegendaryStrengthNames.put(Material.DIAMOND_HELMET, Arrays.asList("Ogre Helmet"));
        orcDefenseLegendaryStrengthNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Ogre Chestplate"));
        orcDefenseLegendaryStrengthNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Ogre Legplates"));
        orcDefenseLegendaryStrengthNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Ogre Greaves"));

        orcDefenseLegendaryDexterityNames = new HashMap<>();
        orcDefenseLegendaryDexterityNames.put(Material.BOW, Arrays.asList("Kneesplitter"));
        orcDefenseLegendaryDexterityNames.put(Material.DIAMOND_HELMET, Arrays.asList("Preystalker Cap"));
        orcDefenseLegendaryDexterityNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Preystalker Coat"));
        orcDefenseLegendaryDexterityNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Preystalker Leggings"));
        orcDefenseLegendaryDexterityNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Preystalker Boots"));

        orcDefenseLegendaryIntellectNames = new HashMap<>();
        orcDefenseLegendaryIntellectNames.put(Material.BLAZE_ROD, Arrays.asList("Totemcarver"));
        orcDefenseLegendaryIntellectNames.put(Material.DIAMOND_HOE, Arrays.asList("Bursting Vulcano"));
        orcDefenseLegendaryIntellectNames.put(Material.DIAMOND_HELMET, Arrays.asList("Earthen Hat"));
        orcDefenseLegendaryIntellectNames.put(Material.DIAMOND_CHESTPLATE, Arrays.asList("Earthen Robe"));
        orcDefenseLegendaryIntellectNames.put(Material.DIAMOND_LEGGINGS, Arrays.asList("Earthen Pants"));
        orcDefenseLegendaryIntellectNames.put(Material.DIAMOND_BOOTS, Arrays.asList("Earthen Shoes"));

        orcOffenseLegendaryOffhandNames = new HashMap<>();
        orcOffenseLegendaryOffhandNames.put(Material.BOOK, Arrays.asList("Three Little Hogs"));
        orcOffenseLegendaryOffhandNames.put(Material.SHIELD, Arrays.asList("CorpseGrinder", "Bulwark of the South"));
        orcOffenseLegendaryOffhandNames.put(Material.SKULL_ITEM, Arrays.asList("Berserker's Skull"));

        orcBalanceLegendaryOffhandNames = new HashMap<>();
        orcBalanceLegendaryOffhandNames.put(Material.BOOK, Arrays.asList("Late Seer's Last Message"));
        orcBalanceLegendaryOffhandNames.put(Material.SHIELD, Arrays.asList("Steel Rooftop"));
        orcBalanceLegendaryOffhandNames.put(Material.SKULL_ITEM, Arrays.asList("Berserker's Skull"));

        orcDefenseLegendaryOffhandNames = new HashMap<>();
        orcDefenseLegendaryOffhandNames.put(Material.BOOK, Arrays.asList("Bibsqeek's Scribbles"));
        orcDefenseLegendaryOffhandNames.put(Material.SHIELD, Arrays.asList("Pavise of Hate"));
        orcDefenseLegendaryOffhandNames.put(Material.SKULL_ITEM, Arrays.asList("Berserker's Skull"));
    }

    public String getOffensePrefix(LegendsRace race, Attribute attribute) {
        switch (race) {
            case HUMAN:
                return humanOffensePrefixes.getOrDefault(attribute, UNIDENTIFIED);
            case ORC:
                return orcOffensePrefixes.getOrDefault(attribute, UNIDENTIFIED);
            default:
                return UNIDENTIFIED;
        }
    }

    public String getBalancePrefix(LegendsRace race, Attribute attribute) {
        switch (race) {
            case HUMAN:
                return humanBalancePrefixes.getOrDefault(attribute, UNIDENTIFIED);
            case ORC:
                return orcBalancePrefixes.getOrDefault(attribute, UNIDENTIFIED);
            default:
                return UNIDENTIFIED;
        }
    }

    public String getDefensePrefix(LegendsRace race, Attribute attribute) {
        switch (race) {
            case HUMAN:
                return humanDefensePrefixes.getOrDefault(attribute, UNIDENTIFIED);
            case ORC:
                return orcDefensePrefixes.getOrDefault(attribute, UNIDENTIFIED);
            default:
                return UNIDENTIFIED;
        }
    }

    public String getStrengthType(Material type) {
        switch (type) {
            case DIAMOND_AXE:
            case GOLD_AXE:
            case IRON_AXE:
            case STONE_AXE:
            case WOOD_AXE:
                return "Axe";
            case DIAMOND_SWORD:
            case GOLD_SWORD:
            case IRON_SWORD:
            case STONE_SWORD:
            case WOOD_SWORD:
                return "Sword";
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
                return "Helmet";
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                return "Chestplate";
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
            case LEATHER_LEGGINGS:
                return "Legplates";
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
                return "Greaves";
            case SHIELD:
                return "Shield";
            case SKULL_ITEM:
                return "Skull";
            default:
                return "Thing";
        }
    }

    public String getDexterityType(Material type) {
        switch (type) {
            case BOW:
                return "Bow";
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
                return "Cap";
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                return "Coat";
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
            case LEATHER_LEGGINGS:
                return "Leggings";
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
                return "Boots";
            default:
                return "Thing";
        }
    }

    public String getIntellectType(Material type) {
        switch (type) {
            case STICK:
                return "Wand";
            case DIAMOND_HOE:
            case GOLD_HOE:
            case IRON_HOE:
            case STONE_HOE:
            case WOOD_HOE:
                return "Staff";
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
            case LEATHER_HELMET:
                return "Hat";
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                return "Robe";
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
            case LEATHER_LEGGINGS:
                return "Pants";
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
            case LEATHER_BOOTS:
                return "Shoes";
            case BOOK:
                return "Book";
            default:
                return "Thing";
        }
    }

    public String getLegendaryOffenseName(LegendsRace race, Attribute primaryStat, Material type) {
        List<String> names;
        switch (race) {
            case HUMAN:
                switch (primaryStat.getName()) {
                    case "Strength":
                        names = humanOffenseLegendaryStrengthNames.get(type);
                        break;
                    case "Dexterity":
                        names = humanOffenseLegendaryDexterityNames.get(type);
                        break;
                    case "Intelligence":
                        names = humanOffenseLegendaryIntellectNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            case ORC:
                switch (primaryStat.getName()) {
                    case "Strength":
                        names = orcOffenseLegendaryStrengthNames.get(type);
                        break;
                    case "Dexterity":
                        names = orcOffenseLegendaryDexterityNames.get(type);
                        break;
                    case "Intelligence":
                        names = orcOffenseLegendaryIntellectNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            default:
                names = Collections.emptyList();
        }
        if (names == null || names.isEmpty()) {
            return "Unidentified Legendary";
        }
        int randomName = random.nextInt(names.size());
        return names.get(randomName);
    }

    public String getLegendaryBalanceName(LegendsRace race, Attribute primaryStat, Material type) {
        List<String> names;
        switch (race) {
            case HUMAN:
                switch (primaryStat.getName()) {
                    case "Strength":
                        names = humanBalanceLegendaryStrengthNames.get(type);
                        break;
                    case "Dexterity":
                        names = humanBalanceLegendaryDexterityNames.get(type);
                        break;
                    case "Intelligence":
                        names = humanBalanceLegendaryIntellectNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            case ORC:
                switch (primaryStat.getName()) {
                    case "Strength":
                        names = orcBalanceLegendaryStrengthNames.get(type);
                        break;
                    case "Dexterity":
                        names = orcBalanceLegendaryDexterityNames.get(type);
                        break;
                    case "Intelligence":
                        names = orcBalanceLegendaryIntellectNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            default:
                names = Collections.emptyList();
        }
        if (names == null || names.isEmpty()) {
            return "Unidentified Legendary";
        }
        int randomName = random.nextInt(names.size());
        return names.get(randomName);
    }

    public String getLegendaryDefenseName(LegendsRace race, Attribute primaryStat, Material type) {
        List<String> names;
        switch (race) {
            case HUMAN:
                switch (primaryStat.getName()) {
                    case "Strength":
                        names = humanDefenseLegendaryStrengthNames.get(type);
                        break;
                    case "Dexterity":
                        names = humanDefenseLegendaryDexterityNames.get(type);
                        break;
                    case "Intelligence":
                        names = humanDefenseLegendaryIntellectNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            case ORC:
                switch (primaryStat.getName()) {
                    case "Strength":
                        names = orcDefenseLegendaryStrengthNames.get(type);
                        break;
                    case "Dexterity":
                        names = orcDefenseLegendaryDexterityNames.get(type);
                        break;
                    case "Intelligence":
                        names = orcDefenseLegendaryIntellectNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            default:
                names = Collections.emptyList();
        }
        if (names == null || names.isEmpty()) {
            return "Unidentified Legendary";
        }
        int randomName = random.nextInt(names.size());
        return names.get(randomName);
    }

    public String getLegendaryOffhandName(LegendsRace race, String rollType, Material type) {
        List<String> names;
        switch (race) {
            case HUMAN:
                switch (rollType) {
                    case "OFFENSE":
                        names = humanOffenseLegendaryOffhandNames.get(type);
                        break;
                    case "BALANCE":
                        names = humanBalanceLegendaryOffhandNames.get(type);
                        break;
                    case "DEFENSE":
                        names = humanDefenseLegendaryOffhandNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            case ORC:
                switch (rollType) {
                    case "OFFENSE":
                        names = orcOffenseLegendaryOffhandNames.get(type);
                        break;
                    case "BALANCE":
                        names = orcBalanceLegendaryOffhandNames.get(type);
                        break;
                    case "DEFENSE":
                        names = orcDefenseLegendaryOffhandNames.get(type);
                        break;
                    default:
                        names = Collections.emptyList();
                }
                break;
            default:
                names = Collections.emptyList();
        }
        if (names == null || names.isEmpty()) {
            return "Unidentified Legendary";
        }
        int randomName = random.nextInt(names.size());
        return names.get(randomName);
    }

    public List<String> getFlavorText(String itemName) {
        String CC = ChatColor.GRAY.toString() + ChatColor.ITALIC;
        switch (itemName) {
            case "The Bloody Baron":
                return Arrays.asList("", CC + "FUCK YOU!");
            case "Excalibur":
                return Arrays.asList("",
                        CC + "...whose power was meant",
                        CC + "to unite all men...");
            case "Truthseeker":
                return Arrays.asList("",
                        CC + "Heard of beasts that are half-lion,",
                        CC + "half-eagle, maidens who are half-fish...",
                        CC + "But you'll never convince me there's",
                        CC + "such a thing as a half-truth.");
            case "Three Little Hogs":
                return Arrays.asList("",
                        CC + "'Punch them in the nose",
                        CC + "Tie them in a knot",
                        CC + "Kick them in the chin",
                        CC + "Put them on the spot'");
            case "Bibsqeek's Scribbles":
                return Arrays.asList("", CC + "The rantings of a lost goblin druid.");
            default:
                return Collections.emptyList();
        }
    }
}
