package gg.warcraft.legends.api.player.data

import java.util.UUID

case class LegendsPlayerData(
    id: UUID,
    tutorialStep: Int = 0,
    combatClassCooldown: Long = 0,
    levelRewardsClaimed: Int = 0,
    launchRewardsClaimed: Boolean = false
)
