package gg.warcraft.legends.app.shop.profile.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileQueryService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;

import java.util.UUID;

public class DefaultShopProfileQueryService implements ShopProfileQueryService {
    private final ShopProfileRepository shopProfileRepository;

    @Inject
    public DefaultShopProfileQueryService(ShopProfileRepository shopProfileRepository) {
        this.shopProfileRepository = shopProfileRepository;
    }

    @Override
    public ShopProfile getProfile(UUID playerId) {
        return shopProfileRepository.getProfile(playerId);
    }
}
