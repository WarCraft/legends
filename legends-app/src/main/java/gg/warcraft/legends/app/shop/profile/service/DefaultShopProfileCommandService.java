package gg.warcraft.legends.app.shop.profile.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;
import gg.warcraft.legends.api.shop.service.ShopRepository;
import gg.warcraft.legends.app.shop.profile.SimpleShopProfile;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public class DefaultShopProfileCommandService implements ShopProfileCommandService {
    private final ShopProfileRepository shopProfileRepository;
    private final ShopRepository shopRepository;
    private final PlayerCommandService playerCommandService;
    private final LegendsPlayerQueryService legendsPlayerQueryService;

    @Inject
    public DefaultShopProfileCommandService(ShopProfileRepository shopProfileRepository, ShopRepository shopRepository,
                                            PlayerCommandService playerCommandService,
                                            LegendsPlayerQueryService legendsPlayerQueryService) {
        this.shopProfileRepository = shopProfileRepository;
        this.shopRepository = shopRepository;
        this.playerCommandService = playerCommandService;
        this.legendsPlayerQueryService = legendsPlayerQueryService;
    }

    @Override
    public void takePayment(ShopItem item, UUID playerId) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(playerId);
        int fragments = player.getFragments();
        int cost = 0; //item.getCost();
        if (fragments < cost) {
            throw new IllegalStateException("Attempted to unlock " + item.getName() + " for player " + playerId +
                    ", but player does not have enough moon fragments!");
        }
        playerCommandService.removeCurrency(playerId, LegendsCurrency.FRAGMENTS, cost);
    }

    @Override
    public void unlockDecoration(DecorationId decorationId, UUID playerId, boolean requiresPayment) {
        ShopItem decoration = shopRepository.getItem(decorationId.toString());
        if (requiresPayment) {
            takePayment(decoration, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<DecorationId> newDecorations = profile.getDecorations();
        newDecorations.add(decorationId);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(newDecorations),
                EnumSet.copyOf(profile.getMiniPets()),
                EnumSet.copyOf(profile.getPromotions()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockDecorationPack(DecorationPackId decorationPackId, UUID playerId, boolean requiresPayment) {
        DecorationPack decorationPack = (DecorationPack) shopRepository.getItem(decorationPackId.toString());
        if (requiresPayment) {
            takePayment(decorationPack, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<DecorationId> newDecorations = profile.getDecorations();
        decorationPack.getDecorations().forEach(decoration ->
                newDecorations.add(DecorationId.valueOf(decoration.getId())));
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(newDecorations),
                EnumSet.copyOf(profile.getMiniPets()),
                EnumSet.copyOf(profile.getPromotions()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockMiniPet(MiniPetId miniPetId, UUID playerId, boolean requiresPayment) {
        ShopItem miniPet = shopRepository.getItem(miniPetId.toString());
        if (requiresPayment) {
            takePayment(miniPet, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<MiniPetId> newMiniPets = profile.getMiniPets();
        newMiniPets.add(miniPetId);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(profile.getDecorations()),
                EnumSet.copyOf(newMiniPets),
                EnumSet.copyOf(profile.getPromotions()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockPromotion(PromotionId promotionId, UUID playerId, boolean requiresPayment) {
        ShopItem promotion = shopRepository.getItem(promotionId.toString());
        if (requiresPayment) {
            takePayment(promotion, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<PromotionId> newPromotions = profile.getPromotions();
        newPromotions.add(promotionId);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(profile.getDecorations()),
                EnumSet.copyOf(profile.getMiniPets()),
                EnumSet.copyOf(newPromotions));
        shopProfileRepository.save(newProfile);
    }
}
