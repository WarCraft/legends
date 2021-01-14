/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.monolith.api.entity.team.Team

import java.util.UUID

case class Npc(
    id: UUID,
    typ: Npc.Type,
    team: Option[Team],
    name: String
)

object Npc {
  type Type = String

  object Type {
    final val BLUEPRINT_VENDOR: Npc.Type = "BLUEPRINT_VENDOR"
    // CLASS_TRAINER, TARGET_DUMMY
  }
}
