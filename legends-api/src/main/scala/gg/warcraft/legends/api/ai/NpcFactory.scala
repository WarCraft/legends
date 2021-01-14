/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai

import gg.warcraft.legends.api.NpcConfig

import java.util.UUID

trait NpcFactory {
  def create(config: NpcConfig): UUID
}
