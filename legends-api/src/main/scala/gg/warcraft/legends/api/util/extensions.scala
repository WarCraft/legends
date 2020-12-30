/*
 * All rights reserved
 *
 * Copyright (c) 2020 WarCraft <https://github.com/WarCraft>
 */

package gg.warcraft.legends.api.util

import gg.warcraft.combat.api.combatclass.CombatClass
import gg.warcraft.legends.api.combat.LegendsCombatArchetype
import gg.warcraft.legends.api.combat.LegendsCombatArchetype._
import gg.warcraft.legends.api.combat.LegendsCombatClass._
import gg.warcraft.legends.api.entity.LegendsTeam
import gg.warcraft.legends.api.player.currency.LegendsCurrency._
import gg.warcraft.legends.api.player.data.{
  LegendsPlayerData, LegendsPlayerDataService
}
import gg.warcraft.legends.api.player.level.{Level, LevelService}
import gg.warcraft.monolith.api.player.Player

object player {
  @inline implicit final class LegendsPlayerOps(val self: Player) extends AnyVal {
    def legendsData(implicit
        legendsDataService: LegendsPlayerDataService
    ): LegendsPlayerData = legendsDataService.legendsData(self.id)

    def legendsTeam: Option[LegendsTeam] =
      self.team.map { _.asInstanceOf[LegendsTeam] }

    def combatClassCooldown(implicit
        legendsDataService: LegendsPlayerDataService
    ): Long = legendsData.combatClassCooldown

    def tutorialStep(implicit
        legendsDataService: LegendsPlayerDataService
    ): Int = legendsData.tutorialStep

    def levelRewardsClaimed(implicit
        legendsDataService: LegendsPlayerDataService
    ): Int = legendsData.levelRewardsClaimed

    def launchRewardsClaimed(implicit
        legendsDataService: LegendsPlayerDataService
    ): Boolean = legendsData.launchRewardsClaimed

    def level(implicit
        levelService: LevelService
    ): Level = levelService.levelForFavor(lifetimeFavor)

    def favor: Int = self.currencies.get(Favor)
    def lifetimeFavor: Int = self.currencies.getLifetime(Favor)

    def fragments: Int = self.currencies.get(Fragments)
    def lifetimeFragments: Int = self.currencies.getLifetime(Fragments)

    def gold: Int = self.currencies.get(Gold)
    def lifetimeGold: Int = self.currencies.getLifetime(Gold)
  }
}

object combatclass {
  @inline implicit final class CombatClassOps(val self: CombatClass) extends AnyVal {
    def archetype: LegendsCombatArchetype = self.id match {
      case Berserker | Guardian  => WARRIOR
      case Marksman | Ranger     => HUNTER
      case Elementalist | Wizard => MAGE
    }
  }
}
