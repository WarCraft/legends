/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.entity

import gg.warcraft.monolith.api.core.ColorCode._
import gg.warcraft.monolith.api.effect.Particle.Color._
import gg.warcraft.monolith.api.entity.team.Team

import scala.util.Random

// TODO remove
sealed trait LegendsTeam {
  val kind: String
}

object LegendsTeam {
  object Dwarves extends Team("Dwarves", GOLD, Orange) with LegendsTeam {
    override val kind: String = "Dwarf"
  }

  object Elves extends Team("Elves", GREEN, Green) with LegendsTeam {
    override val kind: String = "Elf"
  }

  object Humans extends Team("Humans", AQUA, Blue) with LegendsTeam {
    override val kind: String = "Human"
  }

  object Orcs extends Team("Orcs", RED, Red) with LegendsTeam {
    override val kind: String = "Orc"
  }

  object Wayfarers extends Team("Wayfarers", GRAY, Silver) with LegendsTeam {
    override val kind: String = "Wayfarer"
  }

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

  implicit def toString(team: LegendsTeam): String = team.asInstanceOf[Team].name
}
