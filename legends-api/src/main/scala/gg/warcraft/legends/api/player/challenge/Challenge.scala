/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.challenge

import java.util.UUID

case class Challenge(
    id: String,
    statisticId: String,
    name: String,
    description: String,
    requirement: Int,
    reward: Int
) {
  lazy val isDaily: Boolean = id.startsWith("DAILY")
  lazy val isMonthly: Boolean = id.startsWith("MONTHLY")
  lazy val isAlltime: Boolean = id.startsWith("ALLTIME")
}

case class ActiveChallenge(
    challenge: Challenge
) {
  lazy val isDaily: Boolean = challenge.isDaily
  lazy val isMonthly: Boolean = challenge.isMonthly
}

case class CompletedChallenge(
    playerId: UUID,
    challenge: Challenge
) {
  lazy val isDaily: Boolean = challenge.isDaily
  lazy val isMonthly: Boolean = challenge.isMonthly
}
