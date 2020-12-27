/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import gg.warcraft.legends.api.item.ItemRarity

case class ShopConfig(
    prices: Map[ShopItem.Type, Map[ItemRarity, Int]],
    combatClasses: List[CombatClass],
    decorations: List[Decoration],
    decorationPacks: List[DecorationPack],
    miniPets: List[MiniPet],
    promotions: List[Promotion]
)
