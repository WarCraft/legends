package gg.warcraft.legends.api.shop.config;

import gg.warcraft.monolith.api.entity.EntityType;

public interface MiniPetConfiguration extends ShopItemConfiguration {

    EntityType getEntityType();

    boolean isRainbow();
}
