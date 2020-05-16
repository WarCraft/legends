package gg.warcraft.legends.api.combat.passive

import gg.warcraft.combat.api.ability.AbilityPreCastEvent
import gg.warcraft.legends.api.entity.LegendsAttribute
import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.util.typing._

import scala.util.Random

object CriticalPassive {
  private final val random = new Random(System.currentTimeMillis)

  class StatusEffect(
      override val source: CombatSource
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    private final val defaultCrit = 5
    private final val critModifier = 1 // CombatValue.Modifier.percent is additive

    override val name: String = "Critical"
    override val group: String = "legends.passive"
    override val duration: Option[Duration] = None

    override def reduce[T <: PreEvent](event: T): T = event match {
      case event @ AbilityPreCastEvent(_, caster, damageModifiers, _, _) =>
        val crit = caster.attributes.get(LegendsAttribute.Crit)
        val critChance = (crit + defaultCrit) * 0.1f
        if (random.nextFloat <= critChance) {
          val damageModifier = CombatValue.Modifier.percent(source, critModifier)
          event.copy(damageModifiers = damageModifier :: damageModifiers).as[T]
        } else event
      case _ => event
    }
  }
}
