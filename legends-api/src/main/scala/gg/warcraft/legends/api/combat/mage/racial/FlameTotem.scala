package gg.warcraft.legends.api.combat.mage.racial

import gg.warcraft.combat.api.ability.RangedAbility
import gg.warcraft.combat.api.ability.config.AbilitySnapshot
import gg.warcraft.monolith.api.block.Block
import gg.warcraft.monolith.api.entity.Entity

object FlameTotem {
  class Factory extends RangedAbility.Factory {
    override def create(
        caster: Entity,
        snapshot: AbilitySnapshot,
        blockTarget: Option[Block],
        entityTarget: Option[Entity]
    ): RangedAbility = new FlameTotem()
  }
}

class FlameTotem(
    override val caster: Entity,
    override val snapshot: AbilitySnapshot
) extends RangedAbility {
  override def onHit(entity: Entity): Boolean = ???
  override def onMiss(entity: Entity): Boolean = ???
}

/*
public class OrcMagePassive extends AbstractRacialPassive {
    private static final String NAME = "Flame Totem";
    private static final String ABILITY_NAME = "Flame Totem";

    private final AbilityCommandService abilityCommandService;

    @Inject
    public OrcMagePassive(AbilityCommandService abilityCommandService,
                          @Assisted UUID entityId) {
        super(entityId, NAME, null);
        this.abilityCommandService = abilityCommandService;
    }

    @Override
    public void onEntityHealthChangedEvent(EntityHealthChangedEvent event) {
        if (event.getNewPercentHealth() <= 0.75) {
            abilityCommandService.castAbility(ABILITY_NAME, getEntityId(), null);
        }
    }
}
 */