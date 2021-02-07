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
    final val BLUEPRINT_VENDOR: Type = "BLUEPRINT_VENDOR"
    final val FOOD_VENDOR: Type = "FOOD_VENDOR"
    final val TOOL_VENDOR: Type = "TOOL_VENDOR"
    final val WEAPON_VENDOR: Type = "WEAPON_VENDOR"
    // CLASS_TRAINER, TARGET_DUMMY
  }
}
