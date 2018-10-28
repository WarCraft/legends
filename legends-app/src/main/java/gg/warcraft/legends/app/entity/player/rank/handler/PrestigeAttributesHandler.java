package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.legends.api.entity.player.rank.service.RankAttributeModifierRepository;
import gg.warcraft.monolith.api.entity.attribute.Attribute;
import gg.warcraft.monolith.api.entity.attribute.AttributeModifier;
import gg.warcraft.monolith.api.entity.attribute.GenericAttribute;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeCommandService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrestigeAttributesHandler {
    private static final List<Integer> attributeModifierValues;

    static {
        attributeModifierValues = new ArrayList<>();
        attributeModifierValues.add(0);
        attributeModifierValues.add(1);
        attributeModifierValues.add(2);
        attributeModifierValues.add(3);
        attributeModifierValues.add(5);
        attributeModifierValues.add(7);
        attributeModifierValues.add(10);
    }

    private final RankAttributeModifierRepository rankAttributeModifierRepository;
    private final AttributeCommandService attributeCommandService;

    @Inject
    public PrestigeAttributesHandler(RankAttributeModifierRepository rankAttributeModifierRepository,
                                     AttributeCommandService attributeCommandService) {
        this.rankAttributeModifierRepository = rankAttributeModifierRepository;
        this.attributeCommandService = attributeCommandService;
    }

    private AttributeModifier createModifier(UUID playerId, Attribute attribute, float value) {
        return attributeCommandService.addAttributeModifier(playerId, attribute, value);
    }

    @Subscribe
    public void onPlayerRankChangedEvent(PlayerRankChangedEvent event) {
        UUID playerId = event.getPlayerId();
        Rank newRank = event.getNewRank();
        int prestige = newRank.getPrestige();
        int level = newRank.getLevel();
        int attributeValue = attributeModifierValues.get(level / 7);

        // remove existing modifiers
        List<AttributeModifier> modifiers = rankAttributeModifierRepository.getModifiers(playerId);
        modifiers.forEach(modifier -> attributeCommandService.removeAttributeModifier(playerId, modifier));

        // create new modifiers
        List<AttributeModifier> newModifiers = new ArrayList<>();
        if (prestige == 0) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, attributeValue);
            newModifiers.add(vitalityModifier);
        } else if (prestige == 1) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, 6);
            newModifiers.add(vitalityModifier);
            AttributeModifier strengthModifier = createModifier(playerId, LegendsAttribute.STRENGTH, attributeValue);
            newModifiers.add(strengthModifier);
            AttributeModifier dexterityModifier = createModifier(playerId, LegendsAttribute.DEXTERITY, attributeValue);
            newModifiers.add(dexterityModifier);
            AttributeModifier intelligenceModifier =
                    createModifier(playerId, LegendsAttribute.INTELLIGENCE, attributeValue);
            newModifiers.add(intelligenceModifier);
        } else if (prestige == 2) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, 6);
            newModifiers.add(vitalityModifier);
            AttributeModifier strengthModifier = createModifier(playerId, LegendsAttribute.STRENGTH, 6);
            newModifiers.add(strengthModifier);
            AttributeModifier dexterityModifier = createModifier(playerId, LegendsAttribute.DEXTERITY, 6);
            newModifiers.add(dexterityModifier);
            AttributeModifier intelligenceModifier = createModifier(playerId, LegendsAttribute.INTELLIGENCE, 6);
            newModifiers.add(intelligenceModifier);
            AttributeModifier critModifier = createModifier(playerId, LegendsAttribute.CRIT, attributeValue);
            newModifiers.add(critModifier);
        } else {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, 6);
            newModifiers.add(vitalityModifier);
            AttributeModifier strengthModifier = createModifier(playerId, LegendsAttribute.STRENGTH, 6);
            newModifiers.add(strengthModifier);
            AttributeModifier dexterityModifier = createModifier(playerId, LegendsAttribute.DEXTERITY, 6);
            newModifiers.add(dexterityModifier);
            AttributeModifier intelligenceModifier = createModifier(playerId, LegendsAttribute.INTELLIGENCE, 6);
            newModifiers.add(intelligenceModifier);
            AttributeModifier critModifier = createModifier(playerId, LegendsAttribute.CRIT, 6);
            newModifiers.add(critModifier);
        }
        rankAttributeModifierRepository.save(playerId, newModifiers);
    }
}
