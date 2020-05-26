/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.player.data

import java.util.UUID

class LegendsPlayerDataService {

  def legendsData(playerId: UUID): LegendsPlayerData = LegendsPlayerData(playerId)
}
