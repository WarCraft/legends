package gg.warcraft.legends.app.core.handler;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.Specialization;
import gg.warcraft.combat.api.specialization.service.SpecializationQueryService;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilderFactory;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.Equipment;
import gg.warcraft.monolith.api.entity.EquipmentSlot;
import gg.warcraft.monolith.api.world.item.Item;
import gg.warcraft.monolith.api.world.item.ItemFactory;
import gg.warcraft.monolith.api.world.item.ItemType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitCommandHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final SpecializationQueryService specializationQueryService;
    private final Item kitHelmet;
    private final Item kitChestplate;
    private final Item kitLeggings;
    private final Item kitBoots;
    private final Item kitSword;
    private final Item kitAxe;
    private final Item kitBow;
    private final Item kitArrow;
    private final Item kitHoe;
    private final Item kitStick;

    @Inject
    public KitCommandHandler(LegendsPlayerQueryService playerQueryService,
                             LegendsPlayerCommandService playerCommandService,
                             SpecializationQueryService specializationQueryService,
                             EquipmentBuilderFactory equipmentBuilderFactory,
                             ItemFactory itemFactory) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.specializationQueryService = specializationQueryService;

        Map<LegendsAttribute, Integer> kitAttributes = new HashMap<>();
        kitAttributes.put(LegendsAttribute.STRENGTH, 1);
        kitAttributes.put(LegendsAttribute.DEXTERITY, 1);
        kitAttributes.put(LegendsAttribute.INTELLIGENCE, 1);
        kitAttributes.put(LegendsAttribute.VITALITY, 1);
        kitAttributes.put(LegendsAttribute.CRIT, 1);
        this.kitHelmet = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.HELMET, ItemRarity.COMMON, "Beginner's Cap")
                .withAttributes(kitAttributes)
                .build();
        this.kitChestplate = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.CHESTPLATE, ItemRarity.COMMON, "Beginner's Shirt")
                .withAttributes(kitAttributes)
                .build();
        this.kitLeggings = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.LEGGINGS, ItemRarity.COMMON, "Beginner's Shorts")
                .withAttributes(kitAttributes)
                .build();
        this.kitBoots = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.BOOTS, ItemRarity.COMMON, "Beginner's Sandals")
                .withAttributes(kitAttributes)
                .build();
        this.kitSword = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.SWORD, ItemRarity.COMMON, "Beginner's Sword")
                .withAttributes(kitAttributes)
                .build();
        this.kitAxe = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.AXE, ItemRarity.COMMON, "Beginner's Axe")
                .withAttributes(kitAttributes)
                .build();
        this.kitBow = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.BOW, ItemRarity.COMMON, "Beginner's Bow")
                .withAttributes(kitAttributes)
                .build();
        this.kitArrow = itemFactory.create(ItemType.ARROW).withName("Arrow");
        this.kitHoe = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.HOE, ItemRarity.COMMON, "Beginner's Staff")
                .withAttributes(kitAttributes)
                .build();
        this.kitStick = equipmentBuilderFactory.createEquipmentBuilder(
                ItemType.STICK, ItemRarity.COMMON, "Beginner's Wand")
                .withAttributes(kitAttributes)
                .build();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        Equipment equipment = player.getEquipment();
        if (equipment.getSlot(EquipmentSlot.HEAD) == null) {
            playerCommandService.setEquipment(playerId, EquipmentSlot.HEAD, kitHelmet);
        }
        if (equipment.getSlot(EquipmentSlot.CHEST) == null) {
            playerCommandService.setEquipment(playerId, EquipmentSlot.CHEST, kitChestplate);
        }
        if (equipment.getSlot(EquipmentSlot.LEGS) == null) {
            playerCommandService.setEquipment(playerId, EquipmentSlot.LEGS, kitLeggings);
        }
        if (equipment.getSlot(EquipmentSlot.FEET) == null) {
            playerCommandService.setEquipment(playerId, EquipmentSlot.FEET, kitBoots);
        }

        if (player.getInventory().getSpace() < 1) {
            playerCommandService.sendNotification(playerId, "Please make sure you have at least " +
                    "1 free inventory slot to recieve a kit weapon.");
            return true;
        }

        Specialization specialization = player.getSpecialization();
        if (specialization == null) {
            return true;
        }

        switch (specialization.getName()) {
            case "Armsmaster":
                playerCommandService.giveItem(playerId, kitSword, false);
                break;
            case "Berserker":
                playerCommandService.giveItem(playerId, kitAxe, false);
                break;
            case "Hunter":
            case "Marksman":
                playerCommandService.giveItem(playerId, kitBow, false);
                playerCommandService.giveItem(playerId, kitArrow, false);
                break;
            case "Tempest":
                playerCommandService.giveItem(playerId, kitHoe, false);
                break;
            case "Wizard":
                playerCommandService.giveItem(playerId, kitStick, false);
                break;
        }

        return true;
    }
}
