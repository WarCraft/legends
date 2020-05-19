package gg.warcraft.legends.api.player.currency

sealed abstract class LegendsCurrency(val name: String) {
  override def toString: String = name
}

object LegendsCurrency {
  case object Favor extends LegendsCurrency("Favor")
  case object Fragments extends LegendsCurrency("Fragments")
  case object Gold extends LegendsCurrency("Gold")

  implicit def toString(currency: LegendsCurrency): String = currency.toString
}
