package gg.warcraft.legends.api.combat.hunter.marksman

import java.util.UUID

import scala.util.chaining._

class SneakRepository {
  private var _abilities: Map[UUID, Sneak] = Map.empty

  def abilities: Map[UUID, Sneak] = _abilities

  def add(sneak: Sneak): Unit =
    _abilities += (sneak.caster.id -> sneak)

  def remove(casterId: UUID): Option[Sneak] =
    abilities.get(casterId).tap { it => if (it.isDefined) _abilities -= casterId }
}
