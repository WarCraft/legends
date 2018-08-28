package gg.warcraft.legends.api.shop;

import java.util.UUID;

public interface ShopProfile {

    UUID getPlayerId();

    boolean isGrandfathered();

    boolean hasDecoration(Decoration decoration);

    boolean hasMiniPet(MiniPet miniPet);

    boolean hasPromotion(Promotion promotion);
}
