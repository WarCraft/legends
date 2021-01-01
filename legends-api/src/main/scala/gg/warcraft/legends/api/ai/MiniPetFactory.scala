/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai

import gg.warcraft.monolith.api.entity.Entity

import java.util.UUID

trait MiniPetFactory {
  def create(typ: Entity.Type, ownerId: UUID): UUID
  def remove(ownerId: UUID): Unit
  def removeAll(): Unit
}
