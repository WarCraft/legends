package gg.warcraft.legends.app.item.equipment.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.equipment.EquipmentModifier;
import gg.warcraft.legends.api.item.equipment.EquipmentModifierFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentReader;
import gg.warcraft.legends.api.item.equipment.EquipmentReaderFactory;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.entity.event.EntityPreInteractEvent;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;
import gg.warcraft.monolith.api.world.Sound;
import gg.warcraft.monolith.api.world.SoundCategory;
import gg.warcraft.monolith.api.world.WorldService;
import gg.warcraft.monolith.api.world.block.event.BlockPreInteractEvent;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.StackableItem;

import java.util.UUID;

public class EquipmentUpgradeUseHandler {
    private static final String EQUIPMENT_UPGRADE_TYPE = "Equipment Upgrade";
    private static final String EQUIPMENT_TYPE = "Equipment";

    private final WorldService WorldService;
    private final PlayerCommandService playerCommandService;
    private final LegendsPlayerQueryService playerQueryService;
    private final ItemReaderFactory itemReaderFactory;
    private final EquipmentReaderFactory equipmentReaderFactory;
    private final EquipmentModifierFactory equipmentModifierFactory;

    @Inject
    public EquipmentUpgradeUseHandler(WorldService WorldService,
                                      PlayerCommandService playerCommandService,
                                      LegendsPlayerQueryService playerQueryService,
                                      EquipmentModifierFactory equipmentModifierFactory,
                                      EquipmentReaderFactory equipmentReaderFactory,
                                      ItemReaderFactory itemReaderFactory) {
        this.WorldService = WorldService;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.itemReaderFactory = itemReaderFactory;
        this.equipmentReaderFactory = equipmentReaderFactory;
        this.equipmentModifierFactory = equipmentModifierFactory;
    }

    private boolean onInteract(UUID playerId, Item itemInHand) {
        if (itemInHand == null) {
            return false;
        }

        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Item offhand = player.getEquipment().getSlot(EquipmentSlot.OFF_HAND);
        if (offhand == null) {
            return false;
        }

        ItemReader upgradeReader = itemReaderFactory.createItemReader(offhand);
        if (!EQUIPMENT_UPGRADE_TYPE.equals(upgradeReader.getType())) {
            return false;
        }

        EquipmentReader equipmentReader = equipmentReaderFactory.createEquipmentReader(itemInHand);
        if (!EQUIPMENT_TYPE.equals(equipmentReader.getType())) {
            return false;
        }

        if (equipmentReader.getAvailableUpgrades() < 1) {
            return false;
        }

        EquipmentModifier equipmentModifier = equipmentModifierFactory.createEquipmentModifier(itemInHand);
        int strength = upgradeReader.getAttribute(LegendsAttribute.STRENGTH);
        equipmentModifier.addAttribute(LegendsAttribute.STRENGTH, strength);
        int dexterity = upgradeReader.getAttribute(LegendsAttribute.DEXTERITY);
        equipmentModifier.addAttribute(LegendsAttribute.DEXTERITY, dexterity);
        int intelligence = upgradeReader.getAttribute(LegendsAttribute.INTELLIGENCE);
        equipmentModifier.addAttribute(LegendsAttribute.INTELLIGENCE, intelligence);
        int vitality = upgradeReader.getAttribute(LegendsAttribute.VITALITY);
        equipmentModifier.addAttribute(LegendsAttribute.VITALITY, vitality);
        int crit = upgradeReader.getAttribute(LegendsAttribute.CRIT);
        equipmentModifier.addAttribute(LegendsAttribute.CRIT, crit);
        equipmentModifier.addAvailableUpgrades(-1);
        equipmentModifier.addUpgradeCount(1);
        Item newEquipment = equipmentModifier.build();

        StackableItem stackedOffhand = (StackableItem) offhand;
        Item newUpgrade;
        if(stackedOffhand.count() == 1) {
            newUpgrade = null;
        } else {
            newUpgrade = stackedOffhand.withCount(stackedOffhand.count() - 1);
        }

        try {
            playerCommandService.setEquipment(playerId, EquipmentSlot.OFF_HAND, newUpgrade);
            playerCommandService.setEquipment(playerId, EquipmentSlot.MAIN_HAND, newEquipment);

            WorldService.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
                    SoundCategory.AMBIENT, 1, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    @Subscribe
    public void onBlockPreInteractEvent(BlockPreInteractEvent event) {
        UUID playerId = event.getPlayerId();
        Item itemInHand = event.getItemInHand();
        switch (event.getInteraction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (onInteract(playerId, itemInHand)) {
                    event.cancel();
                }
                break;
        }
    }

    @Subscribe
    public void onEntityPreInteractEvent(EntityPreInteractEvent event) {
        UUID playerId = event.getPlayerId();
        Item itemInHand = event.getItemInHand();
        if (onInteract(playerId, itemInHand)) {
            event.cancel();
        }
    }
}
