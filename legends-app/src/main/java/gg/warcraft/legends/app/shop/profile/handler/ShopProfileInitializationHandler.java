package gg.warcraft.legends.app.shop.profile.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;
import gg.warcraft.legends.app.shop.profile.SimpleShopProfile;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;

import java.util.EnumSet;
import java.util.UUID;

public class ShopProfileInitializationHandler {
    private final ShopProfileRepository shopProfileRepository;

    @Inject
    public ShopProfileInitializationHandler(ShopProfileRepository shopProfileRepository) {
        this.shopProfileRepository = shopProfileRepository;
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        if (profile == null) {
            ShopProfile newProfile = new SimpleShopProfile(playerId, false,
                    EnumSet.noneOf(DecorationId.class),
                    EnumSet.noneOf(DecorationPackId.class),
                    EnumSet.noneOf(MiniPetId.class),
                    EnumSet.noneOf(PromotionId.class));
            shopProfileRepository.save(newProfile);
        }
    }
}
