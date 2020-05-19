package gg.warcraft.legends.app.entity.player.rank.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.entity.player.rank.service.RankAttributeModifierRepository;
import gg.warcraft.monolith.api.entity.attribute.AttributeModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class DefaultRankAttributeModifierRepository implements RankAttributeModifierRepository {
    private final Map<UUID, List<AttributeModifier>> modifiers;

    public DefaultRankAttributeModifierRepository() {
        this.modifiers = new HashMap<>();
    }

    @Override
    public List<AttributeModifier> getModifiers(UUID playerId) {
        List<AttributeModifier> playerModifiers = modifiers.computeIfAbsent(playerId, key -> new ArrayList<>());
        return new ArrayList<>(playerModifiers);
    }

    @Override
    public void save(UUID playerId, List<AttributeModifier> modifiers) {
        this.modifiers.put(playerId, new ArrayList<>(modifiers));
    }
}
