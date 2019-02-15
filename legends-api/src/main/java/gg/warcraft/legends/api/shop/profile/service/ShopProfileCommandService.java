package gg.warcraft.legends.api.shop.profile.service;

import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.SpecializationId;

import java.util.UUID;

public interface ShopProfileCommandService {

    void takePayment(ShopItem item, UUID playerId);

    void unlockDecoration(DecorationId id, UUID playerId, boolean requiresPayment);

    void unlockDecorationPack(DecorationPackId id, UUID playerId, boolean requiresPayment);

    void unlockMiniPet(MiniPetId id, UUID playerId, boolean requiresPayment);

    void unlockPromotion(PromotionId id, UUID playerId, boolean requiresPayment);

    void unlockSpecialization(SpecializationId id, UUID playerId, boolean requiresPayment);
}
