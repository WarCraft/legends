/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.leaderboard

import java.util.UUID

class Leaderboard(val entries: List[Leaderboard.Entry])

object Leaderboard {
  class Entry(val playerId: UUID, rank: Int, score: Int)
}
