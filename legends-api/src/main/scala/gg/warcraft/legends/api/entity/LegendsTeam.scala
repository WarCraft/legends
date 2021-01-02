/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.entity.team.{Team, TeamService}

import scala.util.Random

object LegendsTeam {
  type Type = String

  final val Dwarves: Type = "DWARVES"
  final val Elves: Type = "ELVES"
  final val Humans: Type = "HUMANS"
  final val Orcs: Type = "ORCS"
  final val Wayfarers: Type = "WAYFARERS"

  def random(implicit
      teamService: TeamService
  ): Team = Random.nextInt(4) match {
    case 0 => Dwarves.instance
    case 1 => Elves.instance
    case 2 => Humans.instance
    case 3 => Orcs.instance
  }

  @inline implicit final class TeamOps(val self: Team) extends AnyVal {
    def kind: String = self.id match {
      case Dwarves   => "Dwarf"
      case Elves     => "Elf"
      case Humans    => "Human"
      case Orcs      => "Orc"
      case Wayfarers => "Wayfarer"
    }
  }

  @inline implicit final class LegendsTeamOps(val self: Type) extends AnyVal {
    def instance(implicit
        teamService: TeamService
    ): Team = teamService.teams(self)
  }
}
