package gg.warcraft.legends.api.combat

import java.util.UUID

import gg.warcraft.monolith.api.util.Ops._

class KillAssistService {
  private var _killAssists: Map[UUID, Set[UUID]] = Map.empty

  def killAssists: Map[UUID, Set[UUID]] = _killAssists

  def registerAssist(targetId: UUID, attackerId: UUID): Unit =
    _killAssists = _killAssists.updatedWith(targetId) {
      case Some(assists) => (assists + attackerId) |> Some.apply
      case None          => Set(attackerId) |> Some.apply
    }

  def resetAssists(targetId: UUID): Unit =
    _killAssists = _killAssists.updated(targetId, None)
}
