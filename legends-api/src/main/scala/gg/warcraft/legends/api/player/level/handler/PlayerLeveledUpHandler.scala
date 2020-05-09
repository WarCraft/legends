package gg.warcraft.legends.api.player.level.handler

import gg.warcraft.legends.api.player.level.PlayerLevelUpEvent
import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.world.{Sound, SoundCategory, WorldService}

class PlayerLeveledUpHandler(implicit
    worldService: WorldService,
    chatProfileService: ChatProfileService
) extends Event.Handler {
  private final val levelUpSound = Sound.ENTITY_PLAYER_LEVELUP
  private final val soundCategory = SoundCategory.PLAYERS

  override def handle(event: Event): Unit = event match {
    case PlayerLevelUpEvent(player, level) =>
      chatProfileService.save(player.id, s"Lv.${level.value}", level.color)
      worldService.playSound(player.location, levelUpSound, soundCategory)
    case _ =>
  }
}
