package gg.warcraft.legends.api.player.data

import java.time.LocalDate
import java.util.UUID

case class LegendsPlayerData(
    id: UUID,
    tutorialStep: String,
    combatClassCooldown: Long,
    lastDailyReset: LocalDate,
    lastMonthlyReset: LocalDate,
    levelUpRewardsClaimed: Boolean,
    launchRewardsClaimed: Boolean
)
