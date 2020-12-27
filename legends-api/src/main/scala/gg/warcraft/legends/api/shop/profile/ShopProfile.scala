/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.profile

import java.util.UUID

case class ShopProfile(
    playerId: UUID,
    unlocked: Set[String]
){
  lazy val grandfathered:Boolean = unlocked.contains("GRANDFATHERED")
}
