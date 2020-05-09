package gg.warcraft.legends.api.player.data

import java.util.UUID

case class LegendsPlayerData(
    id: UUID,
    tutorialStep: String,
    combatClassCooldown: Long,
    levelRewardsClaimed: Int,
    launchRewardsClaimed: Boolean
)
