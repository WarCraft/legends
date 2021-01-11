/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment;

public enum EquipmentType {
    // Mainhand weapons
    SWORD(false, true),
    AXE(false, true),
    BOW(false, true),
    CROSSBOW(false, true),
    WAND(false, true),
    STAFF(false, true),

    // Offhand weapons
    SHIELD(false, true),
    BOOK(false, true),

    // Armor
    HEAD(true, false),
    CHEST(true, false),
    LEGS(true, false),
    FEET(true, false);

    final boolean isArmor;
    final boolean isWeapon;

    EquipmentType(boolean isArmor, boolean isWeapon) {
        this.isArmor = isArmor;
        this.isWeapon = isWeapon;
    }
}
