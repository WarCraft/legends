package gg.warcraft.legends.api.item.crate

import gg.warcraft.monolith.api.core.event.Event
import gg.warcraft.monolith.api.player.Player

case class LootCrateOpenedEvent(
    player: Player,
    crateType: LootCrate
) extends Event
