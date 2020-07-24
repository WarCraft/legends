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

import scala.util.Random

// TODO remove
sealed abstract class LegendsTeam(
    val kind: String,
    override val name: String,
    override val particleColor: Color,
    override val chatColor: ColorCode
) extends Team

object LegendsTeam {
  case object Dwarves extends LegendsTeam("Dwarf", "Dwarves", Orange, GOLD)
  case object Elves extends LegendsTeam("Elf", "Elves", Green, GREEN)
  case object Humans extends LegendsTeam("Human", "Humans", Blue, AQUA)
  case object Orcs extends LegendsTeam("Orc", "Orcs", Red, RED)
  case object Wayfarers extends LegendsTeam("Wayfarer", "Wayfarers", Silver, GRAY)

  def parseName(name: String): LegendsTeam = name match {
    case "DWARF" => Dwarves
    case "ELF"   => Elves
    case "HUMAN" => Humans
    case "ORC"   => Orcs
  }

  def random: LegendsTeam = Random.nextInt(4) match {
    case 0 => Dwarves
    case 1 => Elves
    case 2 => Humans
    case 3 => Orcs
  }

  implicit def toString(team: LegendsTeam): String = team.toString
}
