package gg.warcraft.legends.app.item.crate.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.crate.LootCrateType;
import gg.warcraft.legends.api.item.crate.config.LootCrateConfiguration;
import gg.warcraft.legends.api.item.crate.config.LootCrateDropTableConfiguration;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.ArrayList;
import java.util.List;

public class JacksonLootCrateConfiguration implements LootCrateConfiguration {
    private final String name;
    private final LootCrateType type;
    private final ItemRarity rarity;
    private final ItemType itemType;
    private final List<LootCrateDropTableConfiguration> dropTable;

    @JsonCreator
    public JacksonLootCrateConfiguration(@JsonProperty("name") String name,
                                         @JsonProperty("type") LootCrateType type,
                                         @JsonProperty("rarity") ItemRarity rarity,
                                         @JsonProperty("itemType") ItemType itemType,
                                         @JsonProperty("dropTable") List<LootCrateDropTableConfiguration> dropTable) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.itemType = itemType;
        this.dropTable = dropTable;
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
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public List<LootCrateDropTableConfiguration> getDropTable() {
        return new ArrayList<>(dropTable);
    }
}
