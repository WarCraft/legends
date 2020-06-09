/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.effect.Particle.Color
import gg.warcraft.monolith.api.effect.Particle.Color._
import gg.warcraft.monolith.api.entity.team.Team

sealed abstract class LegendsTeam(
    val kind: String,
    override val name: String,
    override val particleColor: Color,
    override val chatColor: ColorCode
) extends Team

object LegendsTeam {
  case object Dwarves extends LegendsTeam("Dwarf", "Dwarves", Purple, GOLD)
  case object Elves extends LegendsTeam("Elf", "Elves", Green, GREEN)
  case object Humans extends LegendsTeam("Human", "Humans", Blue, AQUA)
  case object Orcs extends LegendsTeam("Orc", "Orcs", Red, RED)
  case object Wayfarers extends LegendsTeam("Wayfarer", "Wayfarers", Silver, GRAY)

  def parseName(name: String): LegendsTeam = name match {
    case "Dwarves"   => Dwarves
    case "Elves"     => Elves
    case "Humans"    => Humans
    case "Orcs"      => Orcs
    case "Wayfarers" => Wayfarers
  }

  implicit def toString(team: LegendsTeam): String = team.toString
}
