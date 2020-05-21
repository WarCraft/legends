package gg.warcraft.legends.api.player.data

import java.util.UUID

class LegendsPlayerDataService {

  def legendsData(playerId: UUID): LegendsPlayerData = LegendsPlayerData(playerId)
}
