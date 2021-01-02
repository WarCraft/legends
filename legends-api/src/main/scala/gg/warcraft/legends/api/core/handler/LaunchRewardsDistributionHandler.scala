/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.core.handler

import gg.warcraft.legends.api.core.PlayerVoteEvent
import gg.warcraft.legends.api.player.data.LegendsPlayerDataService
import gg.warcraft.legends.api.shop.profile.ShopProfileService
import gg.warcraft.legends.api.util.player._
import gg.warcraft.monolith.api.core.Message
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.PlayerService

class LaunchRewardsDistributionHandler(implicit
    legendsPlayerService: LegendsPlayerDataService,
    shopProfileService: ShopProfileService,
    playerService: PlayerService
) extends Event.Handler {
  private val message = Message.server(
    "You unlocked the Six Year Old Cake. You can equip it from your /me > collectables > promotions menu."
  )

  override def handle(event: Event): Unit = event match {
    case PlayerVoteEvent(player) if !player.launchRewardsClaimed =>
      shopProfileService.unlock(
        "SIX_YEAR_OLD_CAKE",
        player.id,
        requiresPayment = false
      )
      legendsPlayerService.setLaunchRewardsClaimed(player.id, claimed = true)
      playerService.sendMessage(player.id, message)
    case _ =>
  }
}
