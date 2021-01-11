/*
 * All rights reserved
 *
 * Copyright (c) 2021 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.combat.hunter

import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.combat.api.ability.{Ability, ChildAbility, MeleeAbility}

class PetRespawnsRespawn(
    override val parent: Ability,
    override val snapshot: AbilitySnapshot
) extends ChildAbility
    with MeleeAbility

object PetRespawnsRespawn {
  class Factory extends ChildAbility.Factory {
    override def create(
        parent: Ability,
        snapshot: AbilitySnapshot,
        metadata: Map[String, Any]
    ): ChildAbility = new PetRespawnsRespawn(parent, snapshot)
  }
}
