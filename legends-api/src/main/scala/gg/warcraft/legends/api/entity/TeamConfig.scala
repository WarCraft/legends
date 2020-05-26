/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.entity.team.TeamService

class TeamConfig(implicit teamService: TeamService) {
  def writeConfig(): Unit = {
    teamService.registerTeam(LegendsTeam.Dwarves)
    teamService.registerTeam(LegendsTeam.Elves)
    teamService.registerTeam(LegendsTeam.Humans)
    teamService.registerTeam(LegendsTeam.Orcs)
  }
}
