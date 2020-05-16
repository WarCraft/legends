package gg.warcraft.legends.api.combat

sealed abstract class LegendsCombatClass(
    val name: String,
    val typed: LegendsCombatClass.Type
)

object LegendsCombatClass {
  sealed trait Type
  object Type {
    case object Warrior extends Type
    case object Ranger extends Type
    case object Mage extends Type
  }

  case object Guardian extends LegendsCombatClass("Guardian", Type.Warrior)
  case object Berserker extends LegendsCombatClass("Berserker", Type.Warrior)
  case object Hunter extends LegendsCombatClass("Hunter", Type.Ranger)
  case object Marksman extends LegendsCombatClass("Marksman", Type.Ranger)
  case object Elementalist extends LegendsCombatClass("Elementalist", Type.Mage)
  case object Wizard extends LegendsCombatClass("Wizard", Type.Mage)

  implicit def toString(combatClass: LegendsCombatClass): String = combatClass.name
}
