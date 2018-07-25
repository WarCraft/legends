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
