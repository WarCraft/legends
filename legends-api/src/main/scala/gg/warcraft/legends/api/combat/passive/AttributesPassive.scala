package gg.warcraft.legends.api.combat.passive

import gg.warcraft.combat.api.ability.AbilityPreCastEvent
import gg.warcraft.combat.api.data.CombatDataService
import gg.warcraft.combat.api.util.combatplayer._
import gg.warcraft.legends.api.combat.LegendsCombatClass._
import gg.warcraft.legends.api.entity.LegendsAttribute
import gg.warcraft.monolith.api.combat.{CombatSource, CombatValue}
import gg.warcraft.monolith.api.core.Duration
import gg.warcraft.monolith.api.core.event.PreEvent
import gg.warcraft.monolith.api.entity.status.Status
import gg.warcraft.monolith.api.player.PlayerService
import gg.warcraft.monolith.api.util.typing._

object AttributesPassive {
  class StatusEffect(
      override val source: CombatSource
  )(implicit
      playerService: PlayerService,
      combatDataService: CombatDataService
  ) extends Status.Effect {
    require(source.entityId.isDefined)

    private final val strengthClasses = List(Guardian.name, Berserker.name)
    private final val dexterityClasses = List(Hunter.name, Marksman.name)
    private final val intellectClasses = List(Elementalist.name, Wizard.name)

    override val name: String = "Attributes"
    override val group: String = "legends.passive"
    override val duration: Option[Duration] = None

    override def reduce[T <: PreEvent](event: T): T = event match {
      case event @ AbilityPreCastEvent(_, caster, damageModifiers, _, _) =>
        playerService.getPlayerOption(caster.id) match {
          case Some(player) =>
            val modifierValue = player.combatClass match {
              case Some(it) if strengthClasses.contains(it.name) =>
                0.1f * player.attributes.get(LegendsAttribute.Strength)
              case Some(it) if dexterityClasses.contains(it.name) =>
                0.1f * player.attributes.get(LegendsAttribute.Dexterity)
              case Some(it) if intellectClasses.contains(it.name) =>
                0.1f * player.attributes.get(LegendsAttribute.Intellect)
              case _ => 0
            }
            if (modifierValue != 0) {
              val damageModifier = CombatValue.Modifier.flat(source, modifierValue)
              event.copy(damageModifiers = damageModifier :: damageModifiers).as[T]
            } else event
          case None => event
        }
      case _ => event
    }
  }
}
