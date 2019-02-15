package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.SpecializationConfiguration;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;

public class JacksonSpecializationConfiguration extends JacksonShopItemConfiguration implements SpecializationConfiguration {

    @JsonCreator
    public JacksonSpecializationConfiguration(@JsonProperty("id") String id,
                                              @JsonProperty("type") ShopItemType type,
                                              @JsonProperty("rarity") ItemRarity rarity,
                                              @JsonProperty("name") String name,
                                              @JsonProperty("icon") ItemType icon,
                                              @JsonProperty("skull") String skull,
                                              @JsonProperty("lore") List<String> lore) {
        super(id, type, rarity, name, icon, skull, lore);
    }
}
