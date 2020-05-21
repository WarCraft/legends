package gg.warcraft.legends.api.player.level

import gg.warcraft.monolith.api.core.ColorCode

case class Level(
    value: Int,
    color: ColorCode,
    requiredFavor: Int,
    rewards: List[Unit] // TODO
)
