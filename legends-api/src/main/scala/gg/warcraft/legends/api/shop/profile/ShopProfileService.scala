/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop.profile

import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.shop.{DecorationPack, ShopItem, ShopService}
import gg.warcraft.legends.api.util.legendsplayer._
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.player.currency.CurrencyService

import java.util.UUID
import scala.util.chaining.scalaUtilChainingOps

class ShopProfileService(implicit
    unlockedShopItemRepository: UnlockedShopItemRepository,
    shopService: ShopService,
    playerService: PlayerService,
    currencyService: CurrencyService
) {
  private var profiles: Map[UUID, ShopProfile] = Map.empty
  def profile(playerId: UUID): ShopProfile =
    profiles.getOrElse(playerId, ShopProfile(playerId))

  def unlock(
      id: String,
      playerId: UUID,
      requiresPayment: Boolean = true
  ): Boolean = {
    lazy val outdated = profile(playerId)
    if (!requiresPayment || takePayment(id, outdated)) {
      val updated = outdated.copy(unlocked = outdated.unlocked + id)
      profiles += updated
      unlockedShopItemRepository.saveItem(id, playerId)
      true
    } else false
  }

  def calculateDiscount(item: ShopItem, profile: ShopProfile): Int = item match {
    case DecorationPack(_, _, _, price, _, _, _, _, decorations) =>
      val duplicates = decorations.count { it => profile.unlocked.contains(it.id) }
      (duplicates.toFloat / decorations.size.toFloat * price * 0.66f).toInt
    case _ => 0
  }

  private def takePayment(id: String, profile: ShopProfile): Boolean = {
    val item = shopService.itemsById(id)
    val player = playerService.getPlayer(profile.playerId)
    val discountedPrice = item.price - calculateDiscount(item, profile)
    if (player.fragments >= discountedPrice) {
      currencyService.removeCurrency(Fragments, discountedPrice, player.id)
      true
    } else false
  }

  private[shop] def loadProfile(playerId: UUID): Unit =
    unlockedShopItemRepository.loadItems(playerId)
      .pipe { ShopProfile(playerId, _) }
      .tap { it => profiles += (playerId -> it) }

  private[shop] def invalidateProfile(playerId: UUID): Unit =
    profiles -= playerId
}
