package gg.warcraft.legends.app.item.equipment;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import gg.warcraft.legends.api.item.equipment.EquipmentReader;
import gg.warcraft.monolith.api.entity.attribute.Attribute;
import gg.warcraft.monolith.api.item.Item;
import gg.warcraft.monolith.api.item.ItemReader;
import gg.warcraft.monolith.api.item.ItemReaderFactory;
import gg.warcraft.monolith.api.util.StringUtils;

import java.util.List;

public class SimpleEquipmentReader implements EquipmentReader {
    private final ItemReader itemReader;
    private final StringUtils stringUtils;
    private final Item item;

    @Inject
    public SimpleEquipmentReader(ItemReaderFactory itemReaderFactory, StringUtils stringUtils,
                                 @Assisted Item item) {
        this.itemReader = itemReaderFactory.createItemReader(item);
        this.stringUtils = stringUtils;
        this.item = item;
    }

    @Override
    public int getAvailableUpgrades() { // TODO refactor itemReader with a generic extractNumber method which this method and getAttribute can use
        if (item == null) {
            return 0;
        }

        List<String> lore = item.getLore();
        for (String line : lore) {
            if (line.contains("Available Upgrades")) {
                String rawLine = stringUtils.removeChatCodes(line);
                String onlyNumbers = rawLine.replaceAll("[\\D]", "");
                try {
                    return Integer.parseInt(onlyNumbers);
                } catch (NumberFormatException ex) {
                    return 0;
                }
            }
        }
        return 0;
    }

    @Override
    public int getUpgradeCount() {
        if (item == null) {
            return 0;
        }

        final String upgradeCountRegex = ".*\\(\\+\\d*\\)";
        String name = item.getName();
        if (name.matches(upgradeCountRegex)) {
            String onlyUpgradeNumbers = name.replace(upgradeCountRegex, "");
            try {
                return Integer.parseInt(onlyUpgradeNumbers);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }

        return 0;
    }

    @Override
    public String getType() {
        return itemReader.getType();
    }

    @Override
    public int getAttribute(Attribute attribute) {
        return itemReader.getAttribute(attribute);
    }
}
