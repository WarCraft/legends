package gg.warcraft.legends.app.shop.profile.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.SpecializationId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;
import gg.warcraft.legends.api.shop.service.ShopQueryService;
import gg.warcraft.legends.app.shop.profile.SimpleShopProfile;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopProfileInitializationHandler {
    private final ShopProfileRepository shopProfileRepository;
    private final ShopQueryService shopQueryService;

    @Inject
    public ShopProfileInitializationHandler(ShopProfileRepository shopProfileRepository,
                                            ShopQueryService shopQueryService) {
        this.shopProfileRepository = shopProfileRepository;
        this.shopQueryService = shopQueryService;
    }

    private EnumSet<DecorationId> getBasicDecorations() {
        List<DecorationId> basicDecorations = shopQueryService.getDecorationPack("BASIC")
                .getDecorations()
                .stream()
                .map(Decoration::getId)
                .map(DecorationId::valueOf)
                .collect(Collectors.toList());
        return EnumSet.copyOf(basicDecorations);
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        if (profile == null) {
            ShopProfile newProfile = new SimpleShopProfile(playerId, false,
                    getBasicDecorations(),
                    EnumSet.noneOf(MiniPetId.class),
                    EnumSet.noneOf(PromotionId.class),
                    EnumSet.noneOf(SpecializationId.class));
            shopProfileRepository.save(newProfile);
        }
    }
}
