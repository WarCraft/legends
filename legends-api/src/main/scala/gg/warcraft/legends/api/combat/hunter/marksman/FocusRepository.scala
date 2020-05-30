/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter.marksman

import java.util.UUID

import scala.util.chaining._

class FocusRepository {
  private var _abilities: Map[UUID, Focus] = Map.empty

  def abilities: Map[UUID, Focus] = _abilities

  def add(focus: Focus): Unit =
    _abilities += (focus.caster.id -> focus)

  def remove(casterId: UUID): Option[Focus] =
    abilities.get(casterId).tap { it => if (it.isDefined) _abilities -= casterId }
}
