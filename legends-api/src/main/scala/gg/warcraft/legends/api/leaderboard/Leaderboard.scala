package gg.warcraft.legends.api.leaderboard

import java.util.UUID

class Leaderboard(val entries: List[Leaderboard.Entry])

object Leaderboard {
  class Entry(val playerId: UUID, rank: Int, score: Int)
}
