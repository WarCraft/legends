package gg.warcraft.legends.api.combat.hunter.marksman

import java.util.UUID

class FocusRepository {
  private var _abilities: Map[UUID, Focus] = Map.empty

  def abilities: Map[UUID, Focus] = _abilities

  def addFocus(focus: Focus): Unit =
    _abilities += (focus.caster.id -> focus)

  def removeFocus(casterId: UUID): Option[Focus] =
    abilities get casterId match {
      case Some(focus) =>
        _abilities -= casterId
        Some(focus)
      case None => None
    }
}
