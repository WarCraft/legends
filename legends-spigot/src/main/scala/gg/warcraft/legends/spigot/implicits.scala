package gg.warcraft.legends.spigot

import gg.warcraft.legends.api.combat.hunter.marksman.FocusRepository

object implicits {
  def init(): Unit = {}

  implicit lazy val focusRepository: FocusRepository =
    new FocusRepository
}
