package gg.warcraft.legends.app.entity.player.rank.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.event.PlayerRankChangedEvent;
import gg.warcraft.legends.api.entity.player.rank.service.RankAttributeModifierRepository;
import gg.warcraft.legends.api.entity.player.rank.service.RankQueryService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.attribute.Attribute;
import gg.warcraft.monolith.api.entity.attribute.AttributeModifier;
import gg.warcraft.monolith.api.entity.attribute.GenericAttribute;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeCommandService;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PrestigeAttributesHandler {
    public static final List<Integer> attributeModifierValues;

    static {
        attributeModifierValues = new ArrayList<>();
        attributeModifierValues.add(0);
        attributeModifierValues.add(1);
        attributeModifierValues.add(2);
        attributeModifierValues.add(3);
        attributeModifierValues.add(5);
        attributeModifierValues.add(7);
        attributeModifierValues.add(10);
        attributeModifierValues.add(10);
    }

    private final RankAttributeModifierRepository rankAttributeModifierRepository;
    private final RankQueryService rankQueryService;
    private final LegendsPlayerQueryService playerQueryService;
    private final AttributeCommandService attributeCommandService;
    private final TaskService taskService;

    @Inject
    public PrestigeAttributesHandler(RankAttributeModifierRepository rankAttributeModifierRepository,
                                     RankQueryService rankQueryService,
                                     LegendsPlayerQueryService playerQueryService,
                                     AttributeCommandService attributeCommandService,
                                     TaskService taskService) {
        this.rankAttributeModifierRepository = rankAttributeModifierRepository;
        this.rankQueryService = rankQueryService;
        this.playerQueryService = playerQueryService;
        this.attributeCommandService = attributeCommandService;
        this.taskService = taskService;
    }

    private AttributeModifier createModifier(UUID playerId, Attribute attribute, float value) {
        return attributeCommandService.addAttributeModifier(playerId, attribute, value);
    }

    private void updateModifiers(UUID playerId, int prestige, int level) {
        int clippedLevel = level % 50; // NOTE this is hacky, 49/50 is the prestige level
        int attributeValue = attributeModifierValues.get(clippedLevel / 7);

        // remove existing modifiers
        List<AttributeModifier> modifiers = rankAttributeModifierRepository.getModifiers(playerId);
        modifiers.forEach(modifier -> attributeCommandService.removeAttributeModifier(playerId, modifier));

        // create new modifiers
        List<AttributeModifier> newModifiers = new ArrayList<>();
        if (prestige == 0) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, attributeValue);
            newModifiers.add(vitalityModifier);
        } else if (prestige == 1) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, 10);
            newModifiers.add(vitalityModifier);
            AttributeModifier strengthModifier = createModifier(playerId, LegendsAttribute.STRENGTH, attributeValue);
            newModifiers.add(strengthModifier);
            AttributeModifier dexterityModifier = createModifier(playerId, LegendsAttribute.DEXTERITY, attributeValue);
            newModifiers.add(dexterityModifier);
            AttributeModifier intelligenceModifier = createModifier(playerId, LegendsAttribute.INTELLIGENCE, attributeValue);
            newModifiers.add(intelligenceModifier);
        } else if (prestige == 2) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, 10);
            newModifiers.add(vitalityModifier);
            AttributeModifier strengthModifier = createModifier(playerId, LegendsAttribute.STRENGTH, 10);
            newModifiers.add(strengthModifier);
            AttributeModifier dexterityModifier = createModifier(playerId, LegendsAttribute.DEXTERITY, 10);
            newModifiers.add(dexterityModifier);
            AttributeModifier intelligenceModifier = createModifier(playerId, LegendsAttribute.INTELLIGENCE, 10);
            newModifiers.add(intelligenceModifier);
            AttributeModifier critModifier = createModifier(playerId, LegendsAttribute.CRIT, attributeValue);
            newModifiers.add(critModifier);
        } else if (prestige > 2) {
            AttributeModifier vitalityModifier = createModifier(playerId, GenericAttribute.MAX_HEALTH, 10);
            newModifiers.add(vitalityModifier);
            AttributeModifier strengthModifier = createModifier(playerId, LegendsAttribute.STRENGTH, 10);
            newModifiers.add(strengthModifier);
            AttributeModifier dexterityModifier = createModifier(playerId, LegendsAttribute.DEXTERITY, 10);
            newModifiers.add(dexterityModifier);
            AttributeModifier intelligenceModifier = createModifier(playerId, LegendsAttribute.INTELLIGENCE, 10);
            newModifiers.add(intelligenceModifier);
            AttributeModifier critModifier = createModifier(playerId, LegendsAttribute.CRIT, 10);
            newModifiers.add(critModifier);
        }
        rankAttributeModifierRepository.save(playerId, newModifiers);
    }

    @Subscribe
    public void onPlayerRankChangedEvent(PlayerRankChangedEvent event) {
        Rank newRank = event.getNewRank();
        updateModifiers(event.getPlayerId(), newRank.getPrestige(), newRank.getLevel());
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        // TODO split these up in connect and join events, connect is used for initialization, join for populating of data
        taskService.runNextTick(() -> {
            UUID playerId = event.getPlayerId();
            LegendsPlayer player = playerQueryService.getPlayer(playerId);
            if (player == null || player.getTeam() == null || player.getTeam() == LegendsTeam.WAYFARER) {
                return;
            }

            Rank currentRank = rankQueryService.getRank(player.getTeam(), player.getFavor());
            if (currentRank == null) {
                return;
            }

            updateModifiers(playerId, currentRank.getPrestige(), currentRank.getLevel());
        });
    }

    @Subscribe
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        List<AttributeModifier> modifiers = rankAttributeModifierRepository.getModifiers(event.getPlayerId());
        modifiers.forEach(modifier -> attributeCommandService.removeAttributeModifier(event.getPlayerId(), modifier));
    }
}
