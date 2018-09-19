package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.config.PromotionConfiguration;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.item.ItemType;

import java.util.List;
import java.util.Map;

public class JacksonPromotionConfiguration extends JacksonShopItemConfiguration implements PromotionConfiguration {
    private final ItemType itemType;
    private final EquipmentSlot equipmentSlot;
    private final Map<LegendsAttribute, Integer> attributes;

    @JsonCreator
    public JacksonPromotionConfiguration(@JsonProperty("id") String id,
                                         @JsonProperty("type") ShopItemType type,
                                         @JsonProperty("rarity") ItemRarity rarity,
                                         @JsonProperty("name") String name,
                                         @JsonProperty("icon") ItemType icon,
                                         @JsonProperty("skull") String skull,
                                         @JsonProperty("lore") List<String> lore,
                                         @JsonProperty("itemType") ItemType itemType,
                                         @JsonProperty("equipmentSlot") EquipmentSlot equipmentSlot,
                                         @JsonProperty("attributes") Map<LegendsAttribute, Integer> attributes) {
        super(id, type, rarity, name, icon, skull, lore);
        this.itemType = itemType;
        this.equipmentSlot = equipmentSlot;
        this.attributes = attributes;
    }

    @Override
    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    @Override
    public Map<LegendsAttribute, Integer> getAttributes() {
        return attributes;
    }
}
