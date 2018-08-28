package gg.warcraft.legends.api.shop;

import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.monolith.api.item.ItemType;
import gg.warcraft.monolith.api.util.ColorCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Promotion implements ShopItem {
    FOUR_YEAR_OLD_CAKE(1, ItemType.CAKE, "Four Year Old Cake", ItemRarity.RARE,
            "It doesn't look that tasty after all this time,",
            "but its still worth celebrating! Right..?");

    private final int id;
    private final ItemType icon;
    private final String name;
    private final int cost;
    private final List<String> lore;

    Promotion(int id, ItemType icon, String name, ItemRarity rarity, String... flavorText) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        switch (rarity) {
            case RARE:
                this.cost = 1999;
                break;
            default:
                this.cost = Integer.MAX_VALUE;
                break;
        }
        this.lore = new ArrayList<>();
        this.lore.add(ColorCode.GRAY + "Promotional Item");
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
