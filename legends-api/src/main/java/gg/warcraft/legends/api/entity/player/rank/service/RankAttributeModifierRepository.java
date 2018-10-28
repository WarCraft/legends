package gg.warcraft.legends.api.entity.player.rank.service;

import gg.warcraft.monolith.api.entity.attribute.AttributeModifier;

import java.util.List;
import java.util.UUID;

public interface RankAttributeModifierRepository {

    List<AttributeModifier> getModifiers(UUID playerId);

    void save(UUID playerId, List<AttributeModifier> modifiers);
}
