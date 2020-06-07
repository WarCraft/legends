/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.item.equipment.config

class EquipmentConfigRepository {
  private var _config: EquipmentConfig = _

  def config: EquipmentConfig = _config

  def readConfig(config: EquipmentConfig): Unit = _config = config
}
