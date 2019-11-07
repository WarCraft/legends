package gg.warcraft.legends.app.item.crate;

import com.google.common.collect.ImmutableSortedMap;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrate;
import gg.warcraft.legends.api.item.crate.LootCrateDropTable;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.monolith.api.world.item.ItemType;
import gg.warcraft.monolith.api.world.item.ItemTypeOrVariant;

import java.util.SortedMap;

public class SimpleLootCrate implements LootCrate {
    private final String name;
    private final LootCrateType type;
    private final ItemRarity rarity;
    private final ItemTypeOrVariant itemType;
    private final ImmutableSortedMap<Float, LootCrateDropTable> dropTable;

    public SimpleLootCrate(String name, LootCrateType type, ItemRarity rarity, ItemTypeOrVariant itemType,
                           SortedMap<Float, LootCrateDropTable> dropTable) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.itemType = itemType;
        this.dropTable = ImmutableSortedMap.copyOf(dropTable);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LootCrateType getType() {
        return type;
    }

    @Override
    public ItemRarity getRarity() {
        return rarity;
    }

    @Override
    public ItemTypeOrVariant getItemType() {
        return itemType;
    }

    @Override
    public ImmutableSortedMap<Float, LootCrateDropTable> viewDropTable() {
        return dropTable;
    }
}
