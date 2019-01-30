package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.DecorationConfiguration;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;

public class JacksonDecorationConfiguration extends JacksonShopItemConfiguration implements DecorationConfiguration {
    private final ItemType itemType;

    @JsonCreator
    public JacksonDecorationConfiguration(@JsonProperty("id") String id,
                                          @JsonProperty("type") ShopItemType type,
                                          @JsonProperty("rarity") ItemRarity rarity,
                                          @JsonProperty("name") String name,
                                          @JsonProperty("lore") List<String> lore) {
        super(id, type, rarity, name, ItemType.valueOf(id), null, lore);
        this.itemType = getIcon();
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }
}
