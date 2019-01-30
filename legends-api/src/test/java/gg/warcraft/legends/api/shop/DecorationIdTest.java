package gg.warcraft.legends.api.shop;

import gg.warcraft.monolith.api.item.ItemType;
import org.junit.Test;

public class DecorationIdTest {

    @Test
    public void allDecorationIds_ShouldBe_ValidItemTypeIds() {
        for (DecorationId id : DecorationId.values()) {
            ItemType.valueOf(id.name());
        }
    }
}
