/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.ai.npc

import gg.warcraft.legends.api.core.arena.ArenaService
import gg.warcraft.monolith.api.core.ColorCode
import gg.warcraft.monolith.api.item.ItemType
import gg.warcraft.monolith.api.item.variant.SwordVariant
import gg.warcraft.monolith.api.menu.Button.{noop, updateButton}
import gg.warcraft.monolith.api.menu.{Button, Menu, MenuSize}
import gg.warcraft.monolith.api.player.Player

object BattleMasterDialog {
  private def infoButton(implicit
      arenaService: ArenaService
  ): Button = {
    val waiting = arenaService.soloQueueSize + arenaService.duoQueueSize
    Button.noop(
      ItemType.BOOK,
      "Battle Master",
      s"There are $waiting players waiting",
      "to enter the arena."
    )
  }

  private def signUpButton(player: Player)(implicit
      arenaService: ArenaService
  ): Button = Button(
    SwordVariant.IRON,
    "Sign Up",
    s"${ColorCode.AQUA}[Shift Right Click] To sign up",
    s"${ColorCode.AQUA}for a duel in the center arena.",
    "",
    "You'll be matched with whoever",
    "signed up before or after you.",
    "To challenge someone specific",
    "[Shift Right Click] on them to",
    "open their player card."
  ) { click =>
    if (click.isShiftRight) {
      arenaService.joinSoloQueue(player)
      updateButton(signedUpButton(player))
    } else noop
  }

  private def signedUpButton(player: Player)(implicit
      arenaService: ArenaService
  ): Button = {
    val fights = arenaService.approximateFightsBefore(player)
    Button(
      SwordVariant.IRON,
      "Signed Up",
      "You are signed up to fight in",
      "the center arena.",
      s"There are approximately $fights fights",
      "before yours.",
      "",
      s"${ColorCode.AQUA}[Shift Right Click] To chicken out",
      s"${ColorCode.AQUA}and leave the arena queue."
    ) { click =>
      if (click.isShiftRight) {
        arenaService.leaveSoloQueue(player)
        updateButton(signUpButton(player))
      } else noop
    }
  }

  def apply(player: Player)(implicit
      arenaService: ArenaService
  ): Menu = {
    val duelButton =
      if (arenaService.isSignedUp(player)) signedUpButton(player)
      else signUpButton(player)
    Menu(
      "Battle Master",
      MenuSize.ONE_ROW,
      0 -> infoButton,
      2 -> duelButton
    )
  }
}
