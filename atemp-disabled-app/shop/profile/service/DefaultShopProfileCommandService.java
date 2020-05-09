package gg.warcraft.legends.app.shop.profile.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.player.LegendsCurrency;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.shop.Decoration;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPack;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.ShopItem;
import gg.warcraft.legends.api.shop.ShopItemType;
import gg.warcraft.legends.api.shop.SpecializationId;
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
        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        int fragments = player.getFragments();
        int cost;
        if (item.getType() == ShopItemType.DECORATION_PACK) {
            DecorationPack pack = (DecorationPack) item;
            int alreadyUnlocked = (int) pack.getDecorations().stream()
                    .map(Decoration::getId)
                    .map(DecorationId::valueOf)
                    .filter(profile::hasDecoration)
                    .count();
            cost = item.getPrice() - ((int) (item.getPrice() * 0.33f * ((float) alreadyUnlocked / (float) pack.getDecorations().size())));
        } else {
            cost = item.getPrice();
        }
        if (fragments <= cost) {
            throw new IllegalStateException("Attempted to take payment for " + item.getName() + " for player " +
                    playerId + ", but player does not have enough fragments!");
        }
        playerCommandService.removeCurrency(playerId, LegendsCurrency.FRAGMENTS, cost);
    }

    @Override
    public void unlockDecoration(DecorationId id, UUID playerId, boolean requiresPayment) {
        ShopItem decoration = shopRepository.getItem(id.toString());
        if (requiresPayment) {
            takePayment(decoration, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<DecorationId> newDecorations = profile.getDecorations();
        newDecorations.add(id);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(newDecorations),
                EnumSet.copyOf(profile.getMiniPets()),
                EnumSet.copyOf(profile.getPromotions()),
                EnumSet.copyOf(profile.getSpecializations()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockDecorationPack(DecorationPackId id, UUID playerId, boolean requiresPayment) {
        DecorationPack decorationPack = (DecorationPack) shopRepository.getItem(id.toString());
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
                EnumSet.copyOf(profile.getPromotions()),
                EnumSet.copyOf(profile.getSpecializations()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockMiniPet(MiniPetId id, UUID playerId, boolean requiresPayment) {
        ShopItem miniPet = shopRepository.getItem(id.toString());
        if (requiresPayment) {
            takePayment(miniPet, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<MiniPetId> newMiniPets = profile.getMiniPets();
        newMiniPets.add(id);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(profile.getDecorations()),
                EnumSet.copyOf(newMiniPets),
                EnumSet.copyOf(profile.getPromotions()),
                EnumSet.copyOf(profile.getSpecializations()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockPromotion(PromotionId id, UUID playerId, boolean requiresPayment) {
        ShopItem promotion = shopRepository.getItem(id.toString());
        if (requiresPayment) {
            takePayment(promotion, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<PromotionId> newPromotions = profile.getPromotions();
        newPromotions.add(id);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(profile.getDecorations()),
                EnumSet.copyOf(profile.getMiniPets()),
                EnumSet.copyOf(newPromotions),
                EnumSet.copyOf(profile.getSpecializations()));
        shopProfileRepository.save(newProfile);
    }

    @Override
    public void unlockSpecialization(SpecializationId id, UUID playerId, boolean requiresPayment) {
        ShopItem specialization = shopRepository.getItem(id.toString());
        if (requiresPayment) {
            takePayment(specialization, playerId);
        }

        ShopProfile profile = shopProfileRepository.getProfile(playerId);
        Set<SpecializationId> newSpecializations = profile.getSpecializations();
        newSpecializations.add(id);
        ShopProfile newProfile = new SimpleShopProfile(playerId, profile.isGrandfathered(),
                EnumSet.copyOf(profile.getDecorations()),
                EnumSet.copyOf(profile.getMiniPets()),
                EnumSet.copyOf(profile.getPromotions()),
                EnumSet.copyOf(newSpecializations));
        shopProfileRepository.save(newProfile);
    }
}
