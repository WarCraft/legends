package gg.warcraft.legends.api.shop;

import gg.warcraft.monolith.api.entity.EntityType;

public interface MiniPet extends ShopItem {

    EntityType getEntityType();

    boolean isRainbow();
}
