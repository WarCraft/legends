package gg.warcraft.legends.app.shop.profile;

import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class SimpleShopProfile implements ShopProfile {
    private final UUID playerId;
    private final boolean grandfathered;
    private final EnumSet<DecorationId> decorations;
    private final EnumSet<MiniPetId> miniPets;
    private final EnumSet<PromotionId> promotions;

    public SimpleShopProfile(UUID playerId, boolean grandfathered, EnumSet<DecorationId> decorations,
                             EnumSet<MiniPetId> miniPets, EnumSet<PromotionId> promotions) {
        this.playerId = playerId;
        this.grandfathered = grandfathered;
        this.decorations = decorations;
        this.miniPets = miniPets;
        this.promotions = promotions;
    }

    @Override
    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public Set<DecorationId> getDecorations() {
        return EnumSet.copyOf(decorations);
    }

    @Override
    public Set<MiniPetId> getMiniPets() {
        return EnumSet.copyOf(miniPets);
    }

    @Override
    public Set<PromotionId> getPromotions() {
        return EnumSet.copyOf(promotions);
    }

    @Override
    public boolean isGrandfathered() {
        return grandfathered;
    }

    @Override
    public boolean hasDecoration(DecorationId id) {
        return decorations.contains(id);
    }

    @Override
    public boolean hasMiniPet(MiniPetId id) {
        return miniPets.contains(id);
    }

    @Override
    public boolean hasPromotion(PromotionId id) {
        return promotions.contains(id);
    }
}
