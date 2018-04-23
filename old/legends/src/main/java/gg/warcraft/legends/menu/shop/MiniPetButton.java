package gg.warcraft.legends.menu.shop;

import gg.warcraft.monolith.menu.Button;

import java.util.ArrayList;
import java.util.List;

public class MiniPetButton extends Button {

    public MiniPetButton(String skullName, String title, String tooltip, boolean hasUnlocked, Runnable action) {
        super(skullName, title, action);
        List<String> lore = new ArrayList<>();
        lore.add(tooltip);
        lore.add("");
        if (hasUnlocked) {
            lore.add("[SHIFT RIGHT CLICK] to summon me!");
        } else {
            lore.add("You can unlock me from the WarCraft shop!");
        }
        setLoreStrict(lore);
    }
}
