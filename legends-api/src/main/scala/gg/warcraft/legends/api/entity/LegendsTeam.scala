package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.effect.Particle
import gg.warcraft.monolith.api.effect.Particle.Color
import gg.warcraft.monolith.api.entity.team.Team

sealed abstract class LegendsTeam(
    val kind: String,
    override val name: String,
    override val particleColor: Color,
    override val chatColor: ColorCode.Type
) extends Team

object LegendsTeam {
  case object Dwarves
      extends LegendsTeam(
        "Dwarf",
        "Dwarves",
        Particle.Color.PURPLE,
        ColorCode.DARK_PURPLE
      )

  case object Elves
      extends LegendsTeam(
        "Elf",
        "Elves",
        Particle.Color.GREEN,
        ColorCode.DARK_GREEN
      )

  case object Humans
      extends LegendsTeam(
        "Human",
        "Humans",
        Particle.Color.BLUE,
        ColorCode.DARK_BLUE
      )

  case object Orcs
      extends LegendsTeam(
        "Orc",
        "Orcs",
        Particle.Color.RED,
        ColorCode.DARK_RED
      )

  case object Wayfarers
      extends LegendsTeam(
        "Wayfarer",
        "Wayfarers",
        Particle.Color.SILVER,
        ColorCode.GRAY
      )

  implicit def toString(team: LegendsTeam): String = team.toString
}
