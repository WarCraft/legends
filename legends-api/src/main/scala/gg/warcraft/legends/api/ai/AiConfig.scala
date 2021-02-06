/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai

import gg.warcraft.legends.api.ai.npc.Npc
import gg.warcraft.monolith.api.entity.Entity
import gg.warcraft.monolith.api.entity.team.Team
import gg.warcraft.monolith.api.world.Location

case class AiConfig(
    npcs: List[NpcConfig]
)

case class NpcConfig(
    typ: Npc.Type,
    team: Option[Team],
    name: String,
    entityType: Entity.Type,
    location: Location
)
