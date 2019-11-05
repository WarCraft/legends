package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.DecorationPackConfiguration;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.List;

public class JacksonDecorationPackConfiguration extends JacksonShopItemConfiguration implements DecorationPackConfiguration {
    private final List<String> decorations;

    @JsonCreator
    public JacksonDecorationPackConfiguration(@JsonProperty("id") String id,
                                              @JsonProperty("type") ShopItemType type,
                                              @JsonProperty("rarity") ItemRarity rarity,
                                              @JsonProperty("name") String name,
                                              @JsonProperty("icon") ItemType icon,
                                              @JsonProperty("lore") List<String> lore,
                                              @JsonProperty("decorations") List<String> decorations) {
        super(id, type, rarity, name, icon, null, lore);
        this.decorations = decorations;
    }

    @Override
    public List<String> getDecorations() {
        return decorations;
    }
}
