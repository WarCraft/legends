/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core

sealed trait TutorialStep
object TutorialStep {
  case object Start extends TutorialStep { override def toString = "Start" }
  case object Race extends TutorialStep { override def toString = "Race" }
  case object CombatClass extends TutorialStep { override def toString = "Class" }
  case object Gathering extends TutorialStep { override def toString = "Gathering" }
  case object Siege extends TutorialStep { override def toString = "Siege" }
  case object Finale extends TutorialStep { override def toString = "Finale" }
  case object Done extends TutorialStep { override def toString = "Done" }
}
