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
    final val ARENA_BATTLE_MASTER: Type = "BATTLE_MASTER"
    final val ARENA_BETTING_GOBLIN: Type = "BETTING_GOBLIN"

    final val VENDOR_BLUEPRINT: Type = "BLUEPRINT_VENDOR"
    final val VENDOR_FOOD: Type = "FOOD_VENDOR"
    final val VENDOR_TOOL: Type = "TOOL_VENDOR"
    final val VENDOR_WEAPON: Type = "WEAPON_VENDOR"
    // CLASS_TRAINER, TARGET_DUMMY
  }
}
