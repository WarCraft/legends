package gg.warcraft.legends.api.player.statistic

sealed abstract class LegendsStatistic(val id: String) {
  override def toString: String = id
}

object LegendsStatistic {
  // dailies
  case object DailyKills extends LegendsStatistic("DailyKills")
  case object DailyDamage extends LegendsStatistic("DailyDamage")
  case object DailyForts extends LegendsStatistic("DailyForts")
  case object DailyStrongholds extends LegendsStatistic("DailyStrongholds")
  case object DailyGold extends LegendsStatistic("DailyGold")
  case object DailyFavor extends LegendsStatistic("DailyFavor")
  case object DailyPlaytime extends LegendsStatistic("DailyPlaytime")
  case object DailyMobs extends LegendsStatistic("DailyMobs")
  case object DailyResources extends LegendsStatistic("DailyResources")
  case object DailyCrates extends LegendsStatistic("DailyCrates")
  // monthlies
  case object MonthlyKills extends LegendsStatistic("MonthlyKills")
  case object MonthlyDamage extends LegendsStatistic("MonthlyDamage")
  case object MonthlyForts extends LegendsStatistic("MonthlyForts")
  case object MonthlyStrongholds extends LegendsStatistic("MonthlyStrongholds")
  case object MonthlyGold extends LegendsStatistic("MonthlyGold")
  case object MonthlyFavor extends LegendsStatistic("MonthlyFavor")
  case object MonthlyPlaytime extends LegendsStatistic("MonthlyPlaytime")
  case object MonthlyMobs extends LegendsStatistic("MonthlyMobs")
  case object MonthlyResources extends LegendsStatistic("MonthlyResources")
  case object MonthlyCrates extends LegendsStatistic("MonthlyCrates")
  // all-time
  case object AlltimeKills extends LegendsStatistic("AlltimeKills")
  case object AlltimeDamage extends LegendsStatistic("AlltimeDamage")
  case object AlltimeForts extends LegendsStatistic("AlltimeForts")
  case object AlltimeStrongholds extends LegendsStatistic("AlltimeStrongholds")
  case object AlltimeGold extends LegendsStatistic("AlltimeGold")
  case object AlltimeFavor extends LegendsStatistic("AlltimeFavor")
  case object AlltimePlaytime extends LegendsStatistic("AlltimePlaytime")
  case object AlltimeMobs extends LegendsStatistic("AlltimeMobs")
  case object AlltimeResources extends LegendsStatistic("AlltimeResources")
  case object AlltimeCrates extends LegendsStatistic("AlltimeCrates")

  implicit def toString(statistic: LegendsStatistic): String =
    statistic.toString
  implicit def toString(statistics: List[LegendsStatistic]): List[String] =
    statistics.map { _.toString }
}
