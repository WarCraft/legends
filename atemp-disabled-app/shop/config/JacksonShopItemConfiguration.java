package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.ShopItemConfiguration;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.List;

public class JacksonShopItemConfiguration implements ShopItemConfiguration {
    private final String id;
    private final ShopItemType type;
    private final ItemRarity rarity;
    private final String name;
    private final String icon;
    private final String skull;
    private final List<String> lore;

    @JsonCreator
    public JacksonShopItemConfiguration(@JsonProperty("id") String id,
                                        @JsonProperty("type") ShopItemType type,
                                        @JsonProperty("rarity") ItemRarity rarity,
                                        @JsonProperty("name") String name,
                                        @JsonProperty("icon") String icon,
                                        @JsonProperty("skull") String skull,
                                        @JsonProperty("lore") List<String> lore) {
        this.id = id;
        this.type = type;
        this.rarity = rarity;
        this.name = name;
        this.icon = icon;
        this.skull = skull;
        this.lore = lore;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ShopItemType getType() {
        return type;
    }

    @Override
    public ItemRarity getRarity() {
        return rarity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public String getSkull() {
        return skull;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }
}
