package gg.warcraft.legends.api.shop.profile;

import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;

import java.util.Set;
import java.util.UUID;

public interface ShopProfile {

    UUID getPlayerId();

    Set<DecorationId> getDecorations();

    Set<MiniPetId> getMiniPets();

    Set<PromotionId> getPromotions();

    boolean isGrandfathered();

    boolean hasDecoration(DecorationId id);

    boolean hasMiniPet(MiniPetId id);

    boolean hasPromotion(PromotionId id);
}
