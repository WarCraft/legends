package gg.warcraft.legends.api.player.level

import gg.warcraft.legends.api.item.crate.LootCrateType

class LevelService {
  private var _levels: Map[Int, Level] = _
  private var _rewards: Map[Int, Map[LootCrateType, Int]] = _

  def levels: Map[Int, Level] = _levels
  def rewards: Map[Int, Map[LootCrateType, Int]] = _rewards

  def readConfig(config: LegendsConfig): Unit =
    ??? // TODO populate levels
}
