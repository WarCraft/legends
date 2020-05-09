package gg.warcraft.legends.api.player.challenge

case class Challenge(
    id: String,
    statisticId: String,
    name: String,
    description: String,
    requirement: Int,
    reward: Int
)
