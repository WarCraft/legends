/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.shop

import gg.warcraft.legends.api.item.ItemRarity

import java.util.logging.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class ShopService(implicit
    availableShopItemRepository: AvailableShopItemRepository,
    logger: Logger
) {
  private final val premiumTypes: List[ShopItem.Type] =
    ShopItem.CombatClass :: ShopItem.DecorationPack :: ShopItem.MiniPet :: Nil

  private var _itemsById: Map[String, ShopItem] = Map.empty
  def itemsById: Map[String, ShopItem] = _itemsById

  private var _itemsByType: Map[ShopItem.Type, List[ShopItem]] = Map.empty
  def itemsByType: Map[ShopItem.Type, List[ShopItem]] = _itemsByType

  private var _itemsByRarity: Map[ShopItem.Type, Map[ItemRarity, List[ShopItem]]] =
    Map.empty
  def itemsByRarity: Map[ShopItem.Type, Map[ItemRarity, List[ShopItem]]] =
    _itemsByRarity

  private var _priceByTypeAndRarity: Map[ShopItem.Type, Map[ItemRarity, Int]] =
    Map.empty
  def priceByTypeAndRarity: Map[ShopItem.Type, Map[ItemRarity, Int]] =
    _priceByTypeAndRarity

  private var _availableItems: List[ShopItem] = Nil
  def availableItems: List[ShopItem] = Nil

  def readConfig(config: ShopConfig): Unit = {
    import config._

    _itemsById = (
      combatClasses ++ decorations ++ decorationPacks ++ miniPets ++ promotions
    ).map { it => it.id -> it }.toMap

    require(decorations.forall { decoration =>
      val pack = _itemsById(decoration.pack)
      pack.asInstanceOf[DecorationPack].decorations.contains(decoration.id)
    })

    _itemsByType = itemsById.values
      .groupBy { _.typ }
      .map { case (typ, items) => typ -> items.toList }

    _itemsByRarity = itemsByType
      .map { case (typ, items) => typ -> items.groupBy { _.rarity } }

    _priceByTypeAndRarity = config.prices

    _availableItems = availableShopItemRepository.loadAvailable()(this)
    if (availableItems == Nil) rotateAvailable()
  }

  private[shop] def rotateAvailable(): Unit = {
    if (itemsByRarity.isEmpty) {
      logger.warning("No shop items are configured, skipping shop rotation!")
      return
    }

    val filler = (1 to 6).map { _ =>
      val list = itemsByRarity(randomFillerType)(randomFillerRarity)
      list(Random.nextInt(list.size))
    }.toList
    val premium = (1 to 6).map { _ =>
      val list = itemsByRarity(randomPremiumType)(randomPremiumRarity)
      list(Random.nextInt(list.size))
    }.toList

    _availableItems = filler ++ premium

    availableShopItemRepository.deleteAvailable().onComplete { _ =>
      availableShopItemRepository.saveAvailable(_availableItems)
    }
  }

  private def randomFillerType: ShopItem.Type =
    ShopItem.Decoration

  private def randomFillerRarity: ItemRarity =
    ItemRarity.Rare

  private def randomPremiumType: ShopItem.Type =
    if (Random.nextFloat < 0.8) {
      premiumTypes(Random.nextInt(premiumTypes.size))
    } else ShopItem.Decoration

  private def randomPremiumRarity: ItemRarity =
    if (Random.nextFloat < 0.2) ItemRarity.Legendary
    else ItemRarity.Epic
}
