package gg.warcraft.legends.app.item.equipment;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilderFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentModifier;
import gg.warcraft.legends.api.item.equipment.EquipmentReader;
import gg.warcraft.legends.api.item.equipment.EquipmentReaderFactory;
import gg.warcraft.monolith.api.util.ColorCodeUtils;
import gg.warcraft.monolith.api.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class SimpleEquipmentModifier implements EquipmentModifier {
    private final EquipmentBuilderFactory equipmentBuilderFactory;
    private final ColorCodeUtils colorCodeUtils;
    private final Item item;
    private final Map<LegendsAttribute, Integer> modifiedAttributes;

    private int availableUpgrades;
    private int upgradeCount;

    @Inject
    public SimpleEquipmentModifier(EquipmentBuilderFactory equipmentBuilderFactory, EquipmentReaderFactory equipmentReaderFactory,
                                   ColorCodeUtils colorCodeUtils, @Assisted Item item) {
        this.equipmentBuilderFactory = equipmentBuilderFactory;
        this.colorCodeUtils = colorCodeUtils;
        this.item = item;
        this.modifiedAttributes = new HashMap<>();

        EquipmentReader reader = equipmentReaderFactory.createEquipmentReader(item);
        modifiedAttributes.put(LegendsAttribute.STRENGTH, reader.getAttribute(LegendsAttribute.STRENGTH));
        modifiedAttributes.put(LegendsAttribute.DEXTERITY, reader.getAttribute(LegendsAttribute.DEXTERITY));
        modifiedAttributes.put(LegendsAttribute.INTELLIGENCE, reader.getAttribute(LegendsAttribute.INTELLIGENCE));
        modifiedAttributes.put(LegendsAttribute.VITALITY, reader.getAttribute(LegendsAttribute.VITALITY));
        modifiedAttributes.put(LegendsAttribute.CRIT, reader.getAttribute(LegendsAttribute.CRIT));

        this.availableUpgrades = reader.getAvailableUpgrades();
        this.upgradeCount = reader.getUpgradeCount();

        // TODO extract existing item lore and add to builder on build
    }

    @Override
    public EquipmentModifier addAttribute(LegendsAttribute attribute, int amount) {
        int currentAmount = modifiedAttributes.getOrDefault(attribute, 0);
        modifiedAttributes.put(attribute, currentAmount + amount);
        return this;
    }

    @Override
    public EquipmentModifier addAvailableUpgrades(int count) {
        this.availableUpgrades += count;
        return this;
    }

    @Override
    public EquipmentModifier addUpgradeCount(int count) {
        this.upgradeCount += count;
        return this;
    }

    @Override
    public Item build() {
        String itemName = item.name();
        String cleanItemName = colorCodeUtils.stripColorCodes(itemName).replaceAll("\\s\\(\\+\\d+\\)$", "");
        String rarityColorCode = itemName.replace(colorCodeUtils.stripColorCodes(itemName), "");
        ItemRarity itemRarity = ItemRarity.COMMON;
        for (ItemRarity rarity : ItemRarity.values()) {
            if (rarityColorCode.equals(rarity.getColor().toString())) {
                itemRarity = rarity;
            }
        }

        EquipmentBuilder equipmentBuilder = equipmentBuilderFactory.createEquipmentBuilder(item.type(), itemRarity, cleanItemName);
        modifiedAttributes.forEach(equipmentBuilder::withAttribute);
        equipmentBuilder.withAvailableUpgrades(availableUpgrades);
        equipmentBuilder.withUpgradeCount(upgradeCount);
        return equipmentBuilder.build();
    }
}
