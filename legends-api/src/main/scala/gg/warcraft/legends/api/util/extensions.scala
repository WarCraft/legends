package gg.warcraft.legends.api.util

import gg.warcraft.legends.api.player.currency.LegendsCurrency
import gg.warcraft.legends.api.player.data.{
  LegendsPlayerData, LegendsPlayerDataService
}
import gg.warcraft.legends.api.player.level.{Level, LevelService}
import gg.warcraft.monolith.api.player.Player

object legendsplayer {
  @inline implicit final class LegendsPlayerOps(val self: Player) extends AnyVal {
    def legendsData(implicit
        legendsDataService: LegendsPlayerDataService
    ): LegendsPlayerData = legendsDataService.getLegendsData(self.id).getOrThrow

    def combatClassCooldown(implicit
        legendsDataService: LegendsPlayerDataService
    ): Long = legendsData.combatClassCooldown

    def level(implicit
        levelService: LevelService
    ): Level = levelService.levelForFavor(lifetimeFavor)

    def favor: Int = self.currencies.get(LegendsCurrency.FAVOR)
    def fragments: Int = self.currencies.get(LegendsCurrency.FRAGMENTS)
    def gold: Int = self.currencies.get(LegendsCurrency.GOLD)

    def lifetimeFavor: Int =
      self.currencies.getLifetime(LegendsCurrency.FAVOR)
    def lifetimeFragments: Int =
      self.currencies.getLifetime(LegendsCurrency.FRAGMENTS)
    def lifetimeGold: Int =
      self.currencies.getLifetime(LegendsCurrency.GOLD)
  }
}

/*
    TutorialStep getTutorialStep();

    Rank getRank();

    long getSpecializationCooldown();

    int getRankUpRewardsClaimed();
 */
