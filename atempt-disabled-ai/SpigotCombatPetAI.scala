/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.spigot.ai

import org.bukkit.entity.{ Player, Wolf }

class SpigotCombatPetAI(pet: Wolf, owner: Player) extends Runnable {
  private final val maxDistance = 60
  private final val maxCombatDistance = 90

  override def run(): Unit = {
    // TODO if(pet.getLoc)
    // pet.setTarget()
  }

  private def teleportBack(): Unit = {

  }
}
