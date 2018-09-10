package gg.warcraft.legends.app.shop.profile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.shop.DecorationId;
import gg.warcraft.legends.api.shop.DecorationPackId;
import gg.warcraft.legends.api.shop.MiniPetId;
import gg.warcraft.legends.api.shop.PromotionId;
import gg.warcraft.legends.api.shop.profile.ShopProfile;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileRepository;
import gg.warcraft.legends.app.shop.profile.SimpleShopProfile;
import gg.warcraft.legends.app.shop.profile.persistence.ShopProfileItem;
import gg.warcraft.monolith.api.core.JsonMapper;
import gg.warcraft.monolith.api.core.PersistenceService;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class DefaultShopProfileRepository implements ShopProfileRepository {
    private static final String PROFILE_KEY_PREFIX = "shopprofile:";

    private final PersistenceService persistenceService;
    private final ObjectMapper jsonMapper;

    @Inject
    public DefaultShopProfileRepository(PersistenceService persistenceService, @JsonMapper ObjectMapper jsonMapper) {
        this.persistenceService = persistenceService;
        this.jsonMapper = jsonMapper;
    }

    String createProfileKey(UUID playerId) {
        return PROFILE_KEY_PREFIX + playerId;
    }

    ShopProfile mapItemToProfile(ShopProfileItem item) {
        UUID playerId = item.getPlayerId();
        boolean grandfathered = item.isGrandfathered();
        EnumSet<DecorationId> decorations = item.getDecorations().stream()
                .map(DecorationId::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(DecorationId.class)));
        EnumSet<DecorationPackId> decorationPacks = item.getDecorationPacks().stream()
                .map(DecorationPackId::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(DecorationPackId.class)));
        EnumSet<MiniPetId> miniPets = item.getMiniPets().stream()
                .map(MiniPetId::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(MiniPetId.class)));
        EnumSet<PromotionId> promotions = item.getPromotions().stream()
                .map(PromotionId::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PromotionId.class)));
        return new SimpleShopProfile(playerId, grandfathered, decorations, decorationPacks, miniPets, promotions);
    }

    @Override
    public ShopProfile getProfile(UUID playerId) {
        String profileKey = createProfileKey(playerId);
        String profileJson = persistenceService.get(profileKey);
        if (profileJson == null) {
            return null;
        }

        try {
            ShopProfileItem profileItem = jsonMapper.readValue(profileJson, ShopProfileItem.class);
            return mapItemToProfile(profileItem);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    Set<String> mapSetToStringSet(Set<?> set) {
        return set.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    ShopProfileItem mapProfileToItem(ShopProfile profile) {
        UUID playerId = profile.getPlayerId();
        boolean grandfathered = profile.isGrandfathered();
        Set<String> decorations = mapSetToStringSet(profile.getDecorations());
        Set<String> decorationPacks = mapSetToStringSet(profile.getDecorationPacks());
        Set<String> miniPets = mapSetToStringSet(profile.getMiniPets());
        Set<String> promotions = mapSetToStringSet(profile.getPromotions());
        return new ShopProfileItem(playerId, grandfathered, decorations, decorationPacks, miniPets, promotions);
    }

    @Override
    public void save(ShopProfile profile) {
        String profileKey = createProfileKey(profile.getPlayerId());
        ShopProfileItem profileItem = mapProfileToItem(profile);
        try {
            String profileJson = jsonMapper.writeValueAsString(profileItem);
            persistenceService.set(profileKey, profileJson);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
