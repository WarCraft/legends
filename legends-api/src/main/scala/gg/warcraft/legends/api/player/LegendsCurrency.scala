package gg.warcraft.legends.api.player

object LegendsCurrency {
  implicit def toString(currency: LegendsCurrency): String = currency.name
}

sealed abstract class LegendsCurrency(val name: String) {
  override def toString: String = name
}

case object FAVOR extends LegendsCurrency("Favor")
case object FRAGMENTS extends LegendsCurrency("Fragments")
case object GOLD extends LegendsCurrency("Gold")
