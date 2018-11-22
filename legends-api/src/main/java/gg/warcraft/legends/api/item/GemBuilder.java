package gg.warcraft.legends.api.item;

import gg.warcraft.legends.api.entity.LegendsAttribute;
import gg.warcraft.monolith.api.item.Item;

import java.util.Map;

public interface GemBuilder {

    GemBuilder withAttribute(LegendsAttribute attribute, int amount);

    GemBuilder withAttributes(Map<LegendsAttribute, Integer> attributes);

    GemBuilder withSuccessChance(int chance);

    GemBuilder withBreakChance(int chance);

    Item build();
}

/*
        private static final char GEM_CHAR = '❂';

        // add sockets
        int usedSockets = CharMatcher.is(GEM_CHAR).countIn(name);
        if ((usedSockets + availableUpgrades + brokenSockets) > 0) {
            itemBuilder.addLore("");
            String availableSocketText = availableUpgrades == 1
                    ? "Available Socket"
                    : "Available Sockets";
            itemBuilder.addLore(availableUpgrades + " " + availableSocketText);
            String brokenSocketText = brokenSockets == 1
                    ? "Broken Socket"
                    : "Broken Sockets";
            itemBuilder.addLore(brokenSockets + " " + brokenSocketText);
        }
 */
