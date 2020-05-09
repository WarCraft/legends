package gg.warcraft.legends.api.player.statistic

sealed abstract class LegendsStatistic(val id: String) {
  override def toString: String = id
}

object LegendsStatistic {
  // dailies
  case object DAILY_KILLS extends LegendsStatistic("DAILY_KILLS")
  case object DAILY_DAMAGE extends LegendsStatistic("DAILY_DAMAGE")
  case object DAILY_FORTS extends LegendsStatistic("DAILY_FORTS")
  case object DAILY_STRONGHOLDS extends LegendsStatistic("DAILY_STRONGHOLDS")
  case object DAILY_GOLD extends LegendsStatistic("DAILY_GOLD")
  case object DAILY_FAVOR extends LegendsStatistic("DAILY_FAVOR")
  case object DAILY_PLAYTIME extends LegendsStatistic("DAILY_PLAYTIME")
  case object DAILY_MOBS extends LegendsStatistic("DAILY_MOBS")
  case object DAILY_RESOURCES extends LegendsStatistic("DAILY_RESOURCES")
  case object DAILY_CRATES extends LegendsStatistic("DAILY_CRATES")
  // monthlies
  case object MONTHLY_KILLS extends LegendsStatistic("MONTHLY_KILLS")
  case object MONTHLY_DAMAGE extends LegendsStatistic("MONTHLY_DAMAGE")
  case object MONTHLY_FORTS extends LegendsStatistic("MONTHLY_FORTS")
  case object MONTHLY_STRONGHOLDS extends LegendsStatistic("MONTHLY_STRONGHOLDS")
  case object MONTHLY_GOLD extends LegendsStatistic("MONTHLY_GOLD")
  case object MONTHLY_FAVOR extends LegendsStatistic("MONTHLY_FAVOR")
  case object MONTHLY_PLAYTIME extends LegendsStatistic("MONTHLY_PLAYTIME")
  case object MONTHLY_MOBS extends LegendsStatistic("MONTHLY_MOBS")
  case object MONTHLY_RESOURCES extends LegendsStatistic("MONTHLY_RESOURCES")
  case object MONTHLY_CRATES extends LegendsStatistic("MONTHLY_CRATES")
  // all-time
  case object ALLTIME_KILLS extends LegendsStatistic("ALLTIME_KILLS")
  case object ALLTIME_DAMAGE extends LegendsStatistic("ALLTIME_DAMAGE")
  case object ALLTIME_FORTS extends LegendsStatistic("ALLTIME_FORTS")
  case object ALLTIME_STRONGHOLDS extends LegendsStatistic("ALLTIME_STRONGHOLDS")
  case object ALLTIME_GOLD extends LegendsStatistic("ALLTIME_GOLD")
  case object ALLTIME_FAVOR extends LegendsStatistic("ALLTIME_FAVOR")
  case object ALLTIME_PLAYTIME extends LegendsStatistic("ALLTIME_PLAYTIME")
  case object ALLTIME_MOBS extends LegendsStatistic("ALLTIME_MOBS")
  case object ALLTIME_RESOURCES extends LegendsStatistic("ALLTIME_RESOURCES")
  case object ALLTIME_CRATES extends LegendsStatistic("ALLTIME_CRATES")
}
