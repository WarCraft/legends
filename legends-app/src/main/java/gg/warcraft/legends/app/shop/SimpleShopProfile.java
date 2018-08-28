package gg.warcraft.legends.app.shop;

import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.MiniPet;
import gg.warcraft.legends.api.shop.Promotion;
import gg.warcraft.legends.api.shop.ShopProfile;

import java.util.Set;
import java.util.UUID;

public class SimpleShopProfile implements ShopProfile {
    private final UUID playerId;
    private final boolean grandfathered;
    private final Set<Integer> decorations;
    private final Set<Integer> miniPets;
    private final Set<Integer> promotions;

    public SimpleShopProfile(UUID playerId, boolean grandfathered, Set<Integer> decorations, Set<Integer> miniPets,
                             Set<Integer> promotions) {
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
    public boolean isGrandfathered() {
        return grandfathered;
    }

    @Override
    public boolean hasDecoration(Decoration decoration) {
        return decorations.contains(decoration.getId());
    }

    @Override
    public boolean hasMiniPet(MiniPet miniPet) {
        return miniPets.contains(miniPet.getId());
    }

    @Override
    public boolean hasPromotion(Promotion promotion) {
        return promotions.contains(promotion.getId());
    }
}
