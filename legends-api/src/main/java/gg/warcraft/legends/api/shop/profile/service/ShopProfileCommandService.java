package gg.warcraft.legends.api.shop.profile.service;

import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.ShopItem;

import java.util.UUID;

public interface ShopProfileCommandService {

    void takePayment(ShopItem item, UUID playerId);

    void unlockDecoration(DecorationId decorationId, UUID playerId, boolean requiresPayment);

    void unlockDecorationPack(DecorationPackId decorationPackId, UUID playerId, boolean requiresPayment);

    void unlockMiniPet(MiniPetId miniPetId, UUID playerId, boolean requiresPayment);

    void unlockPromotion(PromotionId promotionId, UUID playerId, boolean requiresPayment);
}
