/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.profile

import gg.warcraft.legends.api.shop.ShopConfig

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

class ShopProfileService(implicit
    unlockedShopItemRepository: UnlockedShopItemRepository
) {
  private var profiles: Map[UUID, ShopProfile] = Map.empty
  def profile(playerId: UUID): ShopProfile =
    profiles.getOrElse(playerId, ShopProfile(playerId))

  def readConfig(config: ShopConfig): Unit = {

  }

  def unlock(
      id: String,
      playerId: UUID,
      requiresPayment: Boolean = true
  ): Boolean = {}

  private[shop] def loadProfile(playerId: UUID): Unit =
    unlockedShopItemRepository.loadItems(playerId)
      .pipe { ShopProfile(playerId, _) }
      .tap { it => profiles += (playerId -> it) }

  private[shop] def invalidateProfile(playerId: UUID): Unit =
    profiles -= playerId
}
