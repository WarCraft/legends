package gg.warcraft.legends.api.player.level

import java.util

import gg.warcraft.legends.api.LegendsConfig

class LevelService {
  private val _levelsByFavor: util.NavigableMap[Int, Level] =
    new util.TreeMap[Int, Level]()

  private var _levels: Map[Int, Level] =
    Map.empty

  def levels: Map[Int, Level] = _levels

  def levelForFavor(favor: Int): Level = _levelsByFavor.floorEntry(favor).getValue

  def readConfig(config: LegendsConfig): Unit =
    config.levels.foreach { level =>
      _levels += (level.value -> level)
      _levelsByFavor.put(level.requiredFavor, level)
    }
}
