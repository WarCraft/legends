package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.effect.Particle
import gg.warcraft.monolith.api.effect.Particle.Color
import gg.warcraft.monolith.api.entity.team.Team

object LegendsTeam {
  implicit def toString(team: LegendsTeam): String = team.toString
}

sealed abstract class LegendsTeam(
    val kind: String,
    override val name: String,
    override val particleColor: Color,
    override val chatColor: ColorCode.Type
) extends Team

case object DWARVES
    extends LegendsTeam(
      "Dwarf",
      "Dwarves",
      Particle.Color.PURPLE,
      ColorCode.DARK_PURPLE
    )

case object ELVES
    extends LegendsTeam(
      "Elf",
      "Elves",
      Particle.Color.GREEN,
      ColorCode.DARK_GREEN
    )

case object HUMANS
    extends LegendsTeam(
      "Human",
      "Humans",
      Particle.Color.BLUE,
      ColorCode.DARK_BLUE
    )

case object ORCS
    extends LegendsTeam(
      "Orc",
      "Orcs",
      Particle.Color.RED,
      ColorCode.DARK_RED
    )

case object WAYFARER
    extends LegendsTeam(
      "Wayfarer",
      "Wayfarers",
      Particle.Color.SILVER,
      ColorCode.GRAY
    )
