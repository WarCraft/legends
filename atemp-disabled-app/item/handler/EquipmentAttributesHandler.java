package gg.warcraft.legends.app.item.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.entity.attribute.AttributeModifier;
import gg.warcraft.monolith.api.entity.attribute.GenericAttribute;
import gg.warcraft.monolith.api.entity.attribute.service.AttributeCommandService;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerEquipmentChangedEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;
import gg.warcraft.monolith.api.world.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class EquipmentAttributesHandler {
    private final AttributeCommandService attributeCommandService;
    private final PlayerQueryService playerQueryService;
    private final TaskService taskService;
    private final ItemReaderFactory itemReaderFactory;
    private final Map<UUID, List<AttributeModifier>> equipmentModifiers;

    @Inject
    public EquipmentAttributesHandler(AttributeCommandService attributeCommandService,
                                      PlayerQueryService playerQueryService,
                                      TaskService taskService, ItemReaderFactory itemReaderFactory) {
        this.attributeCommandService = attributeCommandService;
        this.playerQueryService = playerQueryService;
        this.taskService = taskService;
        this.itemReaderFactory = itemReaderFactory;
        this.equipmentModifiers = new HashMap<>();
    }

    void updateEquipmentModifiers(UUID playerId) {
        List<AttributeModifier> currentModifiers = equipmentModifiers.getOrDefault(playerId, Collections.emptyList());
        currentModifiers.forEach(modifier -> attributeCommandService.removeAttributeModifier(playerId, modifier));

        Player player = playerQueryService.getPlayer(playerId);
        int strength = 0;
        int dexterity = 0;
        int intelligence = 0;
        int vitality = 0;
        int crit = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Item equipment = player.getEquipment().getSlot(slot);
            ItemReader reader = itemReaderFactory.createItemReader(equipment);
            strength += reader.getAttribute(LegendsAttribute.STRENGTH);
            dexterity += reader.getAttribute(LegendsAttribute.DEXTERITY);
            intelligence += reader.getAttribute(LegendsAttribute.INTELLIGENCE);
            vitality += reader.getAttribute(LegendsAttribute.VITALITY);
            crit += reader.getAttribute(LegendsAttribute.CRIT);
        }

        AttributeModifier strengthModifier =
                attributeCommandService.addAttributeModifier(playerId, LegendsAttribute.STRENGTH, strength);
        AttributeModifier dexterityModifier =
                attributeCommandService.addAttributeModifier(playerId, LegendsAttribute.DEXTERITY, dexterity);
        AttributeModifier intelligenceModifier =
                attributeCommandService.addAttributeModifier(playerId, LegendsAttribute.INTELLIGENCE, intelligence);
        AttributeModifier vitalityModifier =
                attributeCommandService.addAttributeModifier(playerId, LegendsAttribute.VITALITY, vitality);
        AttributeModifier maxHealthModifier =
                attributeCommandService.addAttributeModifier(playerId, GenericAttribute.MAX_HEALTH, vitality);
        AttributeModifier critModifier =
                attributeCommandService.addAttributeModifier(playerId, LegendsAttribute.CRIT, crit);

        List<AttributeModifier> newModifiers = new ArrayList<>();
        newModifiers.add(strengthModifier);
        newModifiers.add(dexterityModifier);
        newModifiers.add(intelligenceModifier);
        newModifiers.add(vitalityModifier);
        newModifiers.add(maxHealthModifier);
        newModifiers.add(critModifier);
        equipmentModifiers.put(playerId, newModifiers);
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        // await Monolith::AttributesInitializationHandler
        taskService.runNextTick(() -> updateEquipmentModifiers(event.getPlayerId()));
    }

    @Subscribe
    public void onPlayerEquipmentChangedEvent(PlayerEquipmentChangedEvent event) {
        updateEquipmentModifiers(event.getPlayerId());
    }
}
