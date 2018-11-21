package gg.warcraft.legends.app.item.equipment;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.legends.api.item.ItemRarity;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilder;
import gg.warcraft.legends.api.item.equipment.EquipmentBuilderFactory;
import gg.warcraft.legends.api.item.equipment.EquipmentModifier;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;
import gg.warcraft.monolith.api.util.ColorCodeUtils;

import java.util.HashMap;
import java.util.Map;

public class SimpleEquipmentModifier implements EquipmentModifier {
    private final Map<LegendsAttribute, Integer> modifiedAttributes;
    private final EquipmentBuilder equipmentBuilder;

    @Inject
    public SimpleEquipmentModifier(EquipmentBuilderFactory equipmentBuilderFactory, ItemReaderFactory itemReaderFactory,
                                   ColorCodeUtils colorCodeUtils, @Assisted Item item) {
        this.modifiedAttributes = new HashMap<>();

        String itemName = colorCodeUtils.stripColorCodes(item.getName());
        String rarityColorCode = item.getName().replace(itemName, "");
        ItemRarity itemRarity = ItemRarity.COMMON;
        for (ItemRarity rarity : ItemRarity.values()) {
            if (rarityColorCode.equals(rarity.getColor().toString())) {
                itemRarity = rarity;
            }
        }
        this.equipmentBuilder = equipmentBuilderFactory.createEquipmentBuilder(item.getType(), itemRarity, itemName);

        ItemReader reader = itemReaderFactory.createItemReader(item);
        modifiedAttributes.put(LegendsAttribute.STRENGTH, reader.getAttribute(LegendsAttribute.STRENGTH));
        modifiedAttributes.put(LegendsAttribute.DEXTERITY, reader.getAttribute(LegendsAttribute.DEXTERITY));
        modifiedAttributes.put(LegendsAttribute.INTELLIGENCE, reader.getAttribute(LegendsAttribute.INTELLIGENCE));
        modifiedAttributes.put(LegendsAttribute.VITALITY, reader.getAttribute(LegendsAttribute.VITALITY));
        modifiedAttributes.put(LegendsAttribute.CRIT, reader.getAttribute(LegendsAttribute.CRIT));

        // TODO add existing item lore to builder
    }

    @Override
    public EquipmentModifier addAttribute(LegendsAttribute attribute, int amount) {
        int currentAmount = modifiedAttributes.getOrDefault(attribute, 0);
        modifiedAttributes.put(attribute, currentAmount + amount);
        return this;
    }

    @Override
    public Item build() {
        modifiedAttributes.forEach(equipmentBuilder::withAttribute);
        return equipmentBuilder.build();
    }
}
