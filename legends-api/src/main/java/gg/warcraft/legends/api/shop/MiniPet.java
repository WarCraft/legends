package gg.warcraft.legends.api.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MiniPet implements ShopItem {
    // Epic mini pets
    BAT(1, ItemType.SPAWN_BAT, "Bat Pup", ItemRarity.EPIC, ""),
    CHICKEN(2, ItemType.SPAWN_CHICKEN, "Chick", ItemRarity.EPIC, "Why did the chicken cross the road?"),
    COW(3, ItemType.SPAWN_COW, "Calf", ItemRarity.EPIC, ""),
    LLAMA(4, ItemType.SPAWN_LLAMA, "Cria", ItemRarity.EPIC, ""),  // FIXME what about variants
    OCELOT(5, ItemType.SPAWN_OCELOT, "Kitten", ItemRarity.EPIC, ""),
    PIG(6, ItemType.SPAWN_PIG, "Piglet", ItemRarity.EPIC, "Bacon is good for me!"),
    RABBIT(7, ItemType.SPAWN_RABBIT, "Kit", ItemRarity.EPIC, ""),
    SHEEP(8, ItemType.SPAWN_SHEEP, "Lamb", ItemRarity.EPIC, ""),// FIXME what about variants
    SILVERFISH(9, ItemType.SPAWN_SILVERFISH, "Nymph", ItemRarity.EPIC, ""),
    WOLF(10, ItemType.SPAWN_WOLF, "Wolf Pup", ItemRarity.EPIC, ""),

    // Legendary mini pets
    ENDERMITE(100, ItemType.SPAWN_ENDERMITE, "", ItemRarity.LEGENDARY, ""),
    MAGMA_CUBE(101, ItemType.SPAWN_MAGMA_CUBE, "", ItemRarity.LEGENDARY, ""),
    MOOSHROOM(102, ItemType.SPAWN_MOOSHROOM, "", ItemRarity.LEGENDARY, ""),
    PARROT(103, ItemType.SPAWN_PARROT, "", ItemRarity.LEGENDARY, ""),// FIXME what about variants
    PARTICLE_WISP(104, ItemType.BLAZE_POWDER, "", ItemRarity.LEGENDARY, ""),
    POLAR_BEAR(105, ItemType.SPAWN_POLAR_BEAR, "", ItemRarity.LEGENDARY, ""),
    SKELETON_HORSE(106, ItemType.SPAWN_SKELETON_HORSE, "", ItemRarity.LEGENDARY, ""),
    SLIME(107, ItemType.SPAWN_SLIME, "", ItemRarity.LEGENDARY, ""),
    SQUID(108, ItemType.SPAWN_SQUID, "", ItemRarity.LEGENDARY, ""),
    VILLAGER(109, ItemType.SPAWN_VILLAGER, "", ItemRarity.LEGENDARY, ""),
    ZOMBIE_PIGMEN(110, ItemType.SPAWN_ZOMBIE_PIGMAN, "", ItemRarity.LEGENDARY, "Sometimes things just go south. This is sometime."),
    ZOMBIE(111, ItemType.SPAWN_ZOMBIE, "", ItemRarity.LEGENDARY, ""), // FIXME what about variants incl villagers

    // Ancient mini pets
    RAINBOW_BUNCHIE(1000, ItemType.SPAWN_LLAMA, "", ItemRarity.ANCIENT, ""),
    PARTY_PARROT(1001, ItemType.SPAWN_PARROT, "", ItemRarity.ANCIENT, ""),
    RAINBOW_SHEEP(1002, ItemType.SPAWN_SHEEP, "", ItemRarity.ANCIENT, ""),
    CHICKEN_JOCKEY(1003, ItemType.SPAWN_CHICKEN, "", ItemRarity.ANCIENT, ""); // FIXME variants

    private final int id;
    private final ItemType icon;
    private final String name;
    private final int cost;
    private final List<String> lore;

    MiniPet(int id, ItemType icon, String name, ItemRarity rarity, String... flavorText) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        switch (rarity) {
            case EPIC:
                this.cost = 4750;
                break;
            case LEGENDARY:
                this.cost = 9750;
                break;
            case ANCIENT:
                this.cost = 14500;
                break;
            default:
                this.cost = Integer.MAX_VALUE;
                break;
        }
        this.lore = new ArrayList<>();
        this.lore.add(ColorCode.GRAY + "Mini Pet");
        if (flavorText.length > 0) {
            this.lore.add("");
            this.lore.addAll(Arrays.asList(flavorText));
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ItemType getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }
}
