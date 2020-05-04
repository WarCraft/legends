package gg.warcraft.legends.api.player

sealed abstract class LegendsCurrency(val name: String) {
  override def toString: String = name
}

object LegendsCurrency {
  case object FAVOR extends LegendsCurrency("Favor")
  case object FRAGMENTS extends LegendsCurrency("Fragments")
  case object GOLD extends LegendsCurrency("Gold")

  implicit def toString(currency: LegendsCurrency): String = currency.toString
}
