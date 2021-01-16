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
  def availableItems: List[ShopItem] = _availableItems

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

    _availableItems = availableShopItemRepository.loadAll()(this)
    if (availableItems == Nil) rotateAvailable()
  }

  private[shop] def rotateAvailable(): Unit = {
    if (itemsByRarity.isEmpty) {
      logger.warning("No shop items are configured, skipping shop rotation!")
      return
    }

    val filler = randomItems({ randomFillerType _ }, { randomFillerRarity _ })
    val premium = randomItems({ randomPremiumType _ }, { randomPremiumRarity _ })
    _availableItems = filler ++ premium

    availableShopItemRepository.deleteAll().onComplete { _ =>
      availableShopItemRepository.save(_availableItems)
    }
  }

  private def randomFillerType: ShopItem.Type =
    ShopItem.Decoration

  private def randomFillerRarity: ItemRarity =
    ItemRarity.Rare

  private def randomPremiumType: ShopItem.Type = {
    val premiumTypes = List(
      ShopItem.CombatClass,
      ShopItem.Decoration,
      ShopItem.DecorationPack,
      ShopItem.MiniPet
    )
    premiumTypes(Random.nextInt(premiumTypes.size))
  }

  private def randomPremiumRarity: ItemRarity = {
    val premiumRarities = List(
      ItemRarity.Rare,
      ItemRarity.Epic,
      ItemRarity.Legendary
    )
    premiumRarities(Random.nextInt(premiumRarities.size))
  }

  private def randomItems(
      randomType: () => ShopItem.Type,
      randomRarity: () => ItemRarity
  ): List[ShopItem] = {
    var items: List[ShopItem] = Nil
    while (items.length != 7) {
      val item = {
        val typ = randomType()
        var itemPool: List[ShopItem] = Nil
        while (itemPool == Nil) {
          try { itemPool = itemsByRarity(typ)(randomRarity()) }
          catch { case _: Exception => }
        }
        itemPool(Random.nextInt(itemPool.size))
      }
      items = if (!items.contains(item)) item :: items else items
    }
    items
  }
}
