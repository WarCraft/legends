/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.effect.Particle
import gg.warcraft.monolith.api.effect.Particle.Color
import gg.warcraft.monolith.api.entity.team.Team

sealed abstract class LegendsTeam(
    val kind: String,
    override val name: String,
    override val particleColor: Color,
    override val chatColor: ColorCode
) extends Team

object LegendsTeam {
  case object Dwarves
      extends LegendsTeam(
        "Dwarf",
        "Dwarves",
        Particle.Color.Purple,
        ColorCode.LIGHT_PURPLE
      )

  case object Elves
      extends LegendsTeam(
        "Elf",
        "Elves",
        Particle.Color.Green,
        ColorCode.GREEN
      )

  case object Humans
      extends LegendsTeam(
        "Human",
        "Humans",
        Particle.Color.Blue,
        ColorCode.BLUE
      )

  case object Orcs
      extends LegendsTeam(
        "Orc",
        "Orcs",
        Particle.Color.Red,
        ColorCode.RED
      )

  case object Wayfarers
      extends LegendsTeam(
        "Wayfarer",
        "Wayfarers",
        Particle.Color.Silver,
        ColorCode.GRAY
      )

  implicit def toString(team: LegendsTeam): String = team.toString
}
