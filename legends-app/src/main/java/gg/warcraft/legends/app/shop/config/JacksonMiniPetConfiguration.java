package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.MiniPetConfiguration;
import gg.warcraft.monolith.api.entity.EntityType;

import java.util.List;

public class JacksonMiniPetConfiguration extends JacksonShopItemConfiguration implements MiniPetConfiguration {
    private final EntityType entityType;
    private final boolean rainbow;

    @JsonCreator
    public JacksonMiniPetConfiguration(@JsonProperty("id") String id,
                                       @JsonProperty("type") ShopItemType type,
                                       @JsonProperty("rarity") ItemRarity rarity,
                                       @JsonProperty("name") String name,
                                       @JsonProperty("skull") String skull,
                                       @JsonProperty("lore") List<String> lore,
                                       @JsonProperty("entityType") EntityType entityType,
                                       @JsonProperty("rainbow") boolean rainbow) {
        super(id, type, rarity, name, null, skull, lore);
        this.entityType = entityType;
        this.rainbow = rainbow;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }
}
