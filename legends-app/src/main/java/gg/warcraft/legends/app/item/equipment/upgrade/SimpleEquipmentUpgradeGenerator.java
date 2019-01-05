package gg.warcraft.legends.app.item.equipment.upgrade;

import com.google.inject.Inject;
import gg.warcraft.legends.api.combat.LegendsSpecializationType;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeBuilderFactory;
import gg.warcraft.legends.api.item.equipment.upgrade.EquipmentUpgradeGenerator;
import gg.warcraft.monolith.api.item.Item;

import java.util.Random;

public class SimpleEquipmentUpgradeGenerator implements EquipmentUpgradeGenerator {
    private final EquipmentUpgradeBuilderFactory equipmentUpgradeBuilderFactory;
    private final Random random;

    @Inject
    public SimpleEquipmentUpgradeGenerator(EquipmentUpgradeBuilderFactory equipmentUpgradeBuilderFactory,
                                           Random random) {
        this.equipmentUpgradeBuilderFactory = equipmentUpgradeBuilderFactory;
        this.random = random;
    }

    @Override
    public Item generate(LegendsSpecializationType specialization, ItemRarity rarity) {
        LegendsAttribute secondaryStat = random.nextBoolean() ? LegendsAttribute.CRIT : LegendsAttribute.VITALITY;
        switch (specialization) {
            case WARRIOR:
                switch (rarity) {
                    case COMMON:
                    case RARE:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Warrior Scroll")
                                .withAttribute(LegendsAttribute.STRENGTH, 2)
                                .withAttribute(secondaryStat, 1)
                                .build();
                    case EPIC:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Warrior Scroll")
                                .withAttribute(LegendsAttribute.STRENGTH, 3)
                                .withAttribute(secondaryStat, 2)
                                .build();
                    case LEGENDARY:
                    case ANCIENT:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Warrior Scroll")
                                .withAttribute(LegendsAttribute.STRENGTH, 5)
                                .withAttribute(secondaryStat, 3)
                                .build();
                }
                break;
            case RANGER:
                switch (rarity) {
                    case COMMON:
                    case RARE:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Ranger Scroll")
                                .withAttribute(LegendsAttribute.DEXTERITY, 2)
                                .withAttribute(secondaryStat, 1)
                                .build();
                    case EPIC:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Ranger Scroll")
                                .withAttribute(LegendsAttribute.DEXTERITY, 3)
                                .withAttribute(secondaryStat, 2)
                                .build();
                    case LEGENDARY:
                    case ANCIENT:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Ranger Scroll")
                                .withAttribute(LegendsAttribute.DEXTERITY, 5)
                                .withAttribute(secondaryStat, 3)
                                .build();

                }
                break;
            case MAGE:
                switch (rarity) {
                    case COMMON:
                    case RARE:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Mage Scroll")
                                .withAttribute(LegendsAttribute.INTELLIGENCE, 2)
                                .withAttribute(secondaryStat, 1)
                                .build();
                    case EPIC:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Mage Scroll")
                                .withAttribute(LegendsAttribute.INTELLIGENCE, 3)
                                .withAttribute(secondaryStat, 2)
                                .build();
                    case LEGENDARY:
                    case ANCIENT:
                        return equipmentUpgradeBuilderFactory.createEquipmentUpgradeBuilder(rarity, "Mage Scroll")
                                .withAttribute(LegendsAttribute.INTELLIGENCE, 5)
                                .withAttribute(secondaryStat, 3)
                                .build();
                }
                break;
        }
        return null;
    }
}
