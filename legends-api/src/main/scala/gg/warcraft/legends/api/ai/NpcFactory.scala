/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai

import java.util.UUID

trait NpcFactory {
  def create(config: NpcConfig): UUID
}
